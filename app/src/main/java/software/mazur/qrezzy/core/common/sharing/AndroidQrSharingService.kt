package software.mazur.qrezzy.core.common.sharing

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AndroidQrSharingService @Inject constructor(@ApplicationContext private val context: Context) : QrSharingService {
    override fun shareBitmap(bitmap: Bitmap, fileName: String) {
        val safeFileName = fileName.toSafeQrFileName()
        val file = File(context.cacheDir, safeFileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, outputStream)
        }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE_PNG
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(
            Intent.createChooser(intent, null).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        )
    }

    override fun saveBitmap(bitmap: Bitmap, fileName: String): Boolean {
        return try {
            val safeFileName = fileName.toSafeQrFileName()
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, safeFileName)
                put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_PNG)
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/QREZZY")
            }
            val uri = context.contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return false

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, outputStream)
            }
            true
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to save QR code bitmap. fileName=$fileName", exception)
            false
        }
    }

    private fun String.toSafeQrFileName(): String {
        val sanitized = trim()
            .replace(Regex("[^a-zA-Z0-9._-]"), "_")
            .take(MAX_FILE_NAME_LENGTH)
            .ifBlank { DEFAULT_FILE_NAME }

        return "$sanitized.png"
    }

    private companion object {
        const val TAG = "AndroidQrSharingService"
        const val MIME_TYPE_PNG = "image/png"
        const val PNG_QUALITY = 100
        const val DEFAULT_FILE_NAME = "qrezzy_qr_code"
        const val MAX_FILE_NAME_LENGTH = 48
    }
}