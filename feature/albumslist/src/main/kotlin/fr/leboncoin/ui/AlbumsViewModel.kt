package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.analytics.AnalyticsEvent
import fr.leboncoin.common.extensions.stateInWhileSubscribed
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.AnalyticsEventsRepository
import fr.leboncoin.resources.R
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumUIModel
import fr.leboncoin.ui.util.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    val syncState: StateFlow<SyncState> = _syncState.
        stateInWhileSubscribed(
            initialValue = SyncState.Loading,
            scope = viewModelScope
        )

    private val _snackbarEvent = MutableSharedFlow<UiText>()
    val snackbarEvent: SharedFlow<UiText> = _snackbarEvent.asSharedFlow()

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
            val resId = if (!album.isFavourite) R.string.added_to_favourites else R.string.removed_from_favourites
            _snackbarEvent.emit(UiText.StringResource(resId))
        }
    }
}