package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val albumMapper: AlbumUIMapper,
) : ViewModel() {

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    val paginationFlow: Flow<PagingData<AlbumUIModel>> = repository.getFavourites()
        .map { pagingData ->
            pagingData.map { albumMapper.toAlbumUIModel(it) }
        }
        .cachedIn(viewModelScope)

    fun toggleFavourite(album: AlbumUIModel) {
        viewModelScope.launch {
            repository.toggleFavourite(album.id, !album.isFavourite)
            _snackbarEvent.emit(if (!album.isFavourite) "Added to favourites" else "Removed from favourites")
        }
    }
}
