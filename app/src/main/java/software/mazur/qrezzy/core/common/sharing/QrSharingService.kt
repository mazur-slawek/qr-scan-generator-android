package software.mazur.qrezzy.core.common.sharing

import android.graphics.Bitmap

interface QrSharingService {
    fun shareBitmap(bitmap: Bitmap, fileName: String)
    fun saveBitmap(bitmap: Bitmap, fileName: String): Boolean
}