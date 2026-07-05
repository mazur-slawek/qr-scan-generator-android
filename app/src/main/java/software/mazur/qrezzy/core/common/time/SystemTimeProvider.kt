package software.mazur.qrezzy.core.common.time

import software.mazur.qrezzy.domain.common.TimeProvider
import javax.inject.Inject

class SystemTimeProvider
@Inject
constructor() : TimeProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}
