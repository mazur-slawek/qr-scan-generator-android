package software.mazur.qrezzy.core.extensions

fun String.removePrefixIgnoreCase(prefix: String): String {
    return if (startsWith(prefix, ignoreCase = true)) drop(prefix.length) else this
}