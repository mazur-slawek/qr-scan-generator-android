package software.mazur.qrezzy.core.common.time

import javax.inject.Inject

class SystemTimeProvider
    @Inject
    constructor() : TimeProvider {
        override fun nowMillis(): Long = System.currentTimeMillis()
    }
