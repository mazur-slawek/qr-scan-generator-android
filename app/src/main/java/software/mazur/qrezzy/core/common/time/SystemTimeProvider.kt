package software.mazur.qrezzy.core.common.time

import javax.inject.Inject
import software.mazur.qrezzy.domain.common.TimeProvider

class SystemTimeProvider
@Inject
constructor() : TimeProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}
