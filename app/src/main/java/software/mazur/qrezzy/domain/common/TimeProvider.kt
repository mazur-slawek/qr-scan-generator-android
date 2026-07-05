package software.mazur.qrezzy.domain.common

interface TimeProvider {
    fun nowMillis(): Long
}