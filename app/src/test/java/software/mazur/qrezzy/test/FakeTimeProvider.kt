package software.mazur.qrezzy.test

import software.mazur.qrezzy.domain.common.TimeProvider

class FakeTimeProvider(private val now: Long = 123456789L) : TimeProvider {
    override fun nowMillis(): Long = now
}
