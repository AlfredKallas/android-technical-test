package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.common.extensions.stateInWhileSubscribed
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumDetailsUIModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class AlbumDetailsState {
    data object Loading : AlbumDetailsState()
    data class Error(val message: String) : AlbumDetailsState()
    data class Success(val album: AlbumDetailsUIModel) : AlbumDetailsState()
}

@HiltViewModel(assistedFactory = AlbumDetailsViewModel.Factory::class)
class AlbumDetailsViewModel @AssistedInject constructor(
    private val repository: AlbumRepository,
    private val albumMapper: AlbumUIMapper,
    @Assisted private val id: Long
) : ViewModel() {

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    private val _state = MutableStateFlow<AlbumDetailsState>(AlbumDetailsState.Loading)
    val state: StateFlow<AlbumDetailsState> = _state
        .onStart {
            loadAlbumDetails()
        }.stateInWhileSubscribed(
        scope = viewModelScope,
        initialValue = AlbumDetailsState.Loading
    )

    fun loadAlbumDetails() {
        viewModelScope.launch {
            repository.getAlbumDetails(id).collect {
                when (it) {
                    is LCResult.Loading -> _state.value = AlbumDetailsState.Loading
                    is LCResult.Error -> _state.value = AlbumDetailsState.Error(it.exception?.message.orEmpty())
                    is LCResult.Success -> _state.value = AlbumDetailsState.Success(albumMapper.toAlbumDetailsUIModel(it.data))
                }
            }
        }
    }

    fun toggleFavourite(album: AlbumDetailsUIModel) {
        viewModelScope.launch {
            repository.toggleFavourite(album.id, !album.isFavourite)
            _snackbarEvent.emit(if (!album.isFavourite) "Added to favourites" else "Removed from favourites")
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            id: Long,
        ): AlbumDetailsViewModel
    }
}