package software.mazur.qrezzy.domain.settings.usecase

import javax.inject.Inject

class CanSaveQrUseCase @Inject constructor(private val getHistoryLimitStatusUseCase: GetHistoryLimitStatusUseCase) {
    suspend operator fun invoke(): Boolean {
        return !getHistoryLimitStatusUseCase().isLimitReached
    }
}