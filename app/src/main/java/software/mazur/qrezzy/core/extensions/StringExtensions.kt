package software.mazur.qrezzy.core.extensions

fun String.removePrefixIgnoreCase(prefix: String): String = if (startsWith(prefix, ignoreCase = true)) drop(prefix.length) else this
