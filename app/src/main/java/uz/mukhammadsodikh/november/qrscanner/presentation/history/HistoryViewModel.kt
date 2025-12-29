package uz.mukhammadsodikh.november.qrscanner.presentation.history


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.mukhammadsodikh.november.qrscanner.data.local.dao.QRDatabase
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode
import uz.mukhammadsodikh.november.qrscanner.domain.repository.HistoryRepository

class HistoryViewModel(context: Context) : ViewModel() {

    private val repository = HistoryRepository(
        QRDatabase.getInstance(context).qrHistoryDao()
    )

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getAllHistory()
                .combine(_uiState.map { it.selectedFilter }) { history, filter ->
                    history to filter
                }
                .combine(_uiState.map { it.searchQuery }) { (history, filter), query ->
                    Triple(history, filter, query)
                }
                .collectLatest { (history, filter, query) ->
                    val filtered = applyFilters(history, filter, query)
                    _uiState.update {
                        it.copy(
                            historyList = history,
                            filteredList = filtered,
                            isEmpty = filtered.isEmpty(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun applyFilters(
        history: List<QRCode>,
        filter: HistoryFilter,
        query: String
    ): List<QRCode> {
        var result = history

        // Apply filter
        result = when (filter) {
            is HistoryFilter.All -> result
            is HistoryFilter.Favorites -> result.filter { it.isFavorite }
            is HistoryFilter.ByType -> result.filter { it.getTitle() == filter.type }
        }

        // Apply search
        if (query.isNotBlank()) {
            result = result.filter {
                it.rawContent.contains(query, ignoreCase = true) ||
                        it.getTitle().contains(query, ignoreCase = true)
            }
        }

        return result
    }

    fun setFilter(filter: HistoryFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleFavorite(qrCode: QRCode) {
        viewModelScope.launch {
            repository.toggleFavorite(qrCode.id)
        }
    }

    fun deleteItem(qrCode: QRCode) {
        viewModelScope.launch {
            repository.delete(qrCode)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun selectItem(qrCode: QRCode?) {
        _uiState.update {
            it.copy(
                selectedItem = qrCode,
                showDetailDialog = qrCode != null
            )
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { it.copy(showDeleteDialog = show) }
    }

    fun dismissDetailDialog() {
        _uiState.update {
            it.copy(
                showDetailDialog = false,
                selectedItem = null
            )
        }
    }
}