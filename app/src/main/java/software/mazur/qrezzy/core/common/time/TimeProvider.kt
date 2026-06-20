package software.mazur.qrezzy.core.common.time

interface TimeProvider {
    fun nowMillis(): Long
}
