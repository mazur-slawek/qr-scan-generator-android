package software.mazur.qrezzy.feature.history.model

import androidx.compose.runtime.Immutable
import software.mazur.qrezzy.domain.qr.model.Qr

@Immutable
data class HistorySectionUi(val date: String, val items: List<Qr>)
