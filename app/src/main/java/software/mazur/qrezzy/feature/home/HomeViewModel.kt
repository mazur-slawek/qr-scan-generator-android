package software.mazur.qrezzy.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _selectedTab = MutableStateFlow(HomeTab.SCAN)
    val selectedTab = _selectedTab.asStateFlow()

    fun onTabSelected(tab: HomeTab) {
        _selectedTab.value = tab
    }
}
