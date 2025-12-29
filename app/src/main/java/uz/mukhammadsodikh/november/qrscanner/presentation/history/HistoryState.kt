package uz.mukhammadsodikh.november.qrscanner.presentation.history

import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode

/**
 * Filter type
 */
sealed class HistoryFilter {
    object All : HistoryFilter()
    object Favorites : HistoryFilter()
    data class ByType(val type: String) : HistoryFilter()
}

/**
 * History UI State
 */
data class HistoryUiState(
    val historyList: List<QRCode> = emptyList(),
    val filteredList: List<QRCode> = emptyList(),
    val selectedFilter: HistoryFilter = HistoryFilter.All,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val selectedItem: QRCode? = null,
    val showDeleteDialog: Boolean = false,
    val showDetailDialog: Boolean = false
)