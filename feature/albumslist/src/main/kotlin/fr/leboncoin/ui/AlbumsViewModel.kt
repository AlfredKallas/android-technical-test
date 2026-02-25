package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.analytics.AnalyticsEvent
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.AnalyticsEventsRepository
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumUIModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SyncState {
    data object Loading : SyncState()
    data class Error(val message: String) : SyncState()
    data object Success : SyncState()
}

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val analyticsRepository: AnalyticsEventsRepository,
    private val albumMapper: AlbumUIMapper
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Loading)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            repository.sync().collect {
                _syncState.value = when (it) {
                    is LCResult.Loading -> SyncState.Loading
                    is LCResult.Error -> SyncState.Error(it.exception?.message.orEmpty())
                    is LCResult.Success -> SyncState.Success
                }
            }
        }
    }

    val paginationFlow = repository.getAlbums()
        .map { pagingData ->
            pagingData.map { albumEntity ->
                albumMapper.toAlbumUIModel(albumEntity)
            }
        }
        .cachedIn(viewModelScope)

    fun trackEventOnItemSelected(id: String) {
        analyticsRepository.logEvent(
            AnalyticsEvent(
                AnalyticsEvent.AnalyticsType.UserInteraction("AlbumsScreen"),
                listOf(AnalyticsEvent.Param("item_id", id))
            )
        )
    }

    fun toggleFavourite(album: AlbumUIModel) {
        viewModelScope.launch {
            repository.toggleFavourite(album.id, !album.isFavourite)
            _snackbarEvent.emit(if (!album.isFavourite) "Added to favourites" else "Removed from favourites")
        }
    }
}