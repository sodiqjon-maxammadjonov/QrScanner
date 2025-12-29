package uz.mukhammadsodikh.november.qrscanner.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import uz.mukhammadsodikh.november.qrscanner.core.design.components.*
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.presentation.history.components.HistoryItemCard
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components.QRResultCard

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = run {
        val context = LocalContext.current
        androidx.lifecycle.viewmodel.compose.viewModel(
            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(context) as T
                }
            }
        )
    }
) {
    val spacing = LocalSpacing.current
    val uiState by viewModel.uiState.collectAsState()
    var showFilterMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    TitleText(text = "History")
                    SubtitleText(text = "${uiState.filteredList.size} items")
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                ) {
                    // Filter button
                    IconButton(onClick = { showFilterMenu = !showFilterMenu }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }

                    // Delete all button
                    IconButton(
                        onClick = { viewModel.showDeleteDialog(true) },
                        enabled = uiState.historyList.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Delete All",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Filter dropdown
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            viewModel.setFilter(HistoryFilter.All)
                            showFilterMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Favorites") },
                        onClick = {
                            viewModel.setFilter(HistoryFilter.Favorites)
                            showFilterMenu = false
                        }
                    )
                }
            }

            // Search bar
            InputField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = "Search...",
                modifier = Modifier.padding(horizontal = spacing.spaceMedium)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            // List
            if (uiState.isEmpty) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        SubtitleText(
                            text = if (uiState.searchQuery.isNotBlank()) "No results found" else "No history yet",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(spacing.spaceMedium),
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                ) {
                    items(
                        items = uiState.filteredList,
                        key = { it.id }
                    ) { qrCode ->
                        HistoryItemCard(
                            qrCode = qrCode,
                            onItemClick = { viewModel.selectItem(qrCode) },
                            onFavoriteClick = { viewModel.toggleFavorite(qrCode) },
                            onDeleteClick = { viewModel.deleteItem(qrCode) }
                        )
                    }
                }
            }
        }

        // Detail dialog
        if (uiState.showDetailDialog && uiState.selectedItem != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f))
                    .padding(spacing.spaceMedium),
                contentAlignment = Alignment.Center
            ) {
                QRResultCard(
                    qrCode = uiState.selectedItem!!,
                    onDismiss = { viewModel.dismissDetailDialog() }
                )
            }
        }

        // Delete all confirmation dialog
        if (uiState.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showDeleteDialog(false) },
                title = { Text("Delete All History?") },
                text = { Text("This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteAll()
                            viewModel.showDeleteDialog(false)
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showDeleteDialog(false) }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}