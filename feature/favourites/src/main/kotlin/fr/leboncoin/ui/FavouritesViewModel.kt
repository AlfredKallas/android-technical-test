package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.common.network.DefaultDispatcher
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumUIModel
import fr.leboncoin.ui.util.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val albumMapper: AlbumUIMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val paginationFlow: Flow<PagingData<AlbumUIModel>> = repository.getFavourites()
        .map { pagingData ->
            pagingData.map { album ->
                albumMapper.toAlbumUIModel(album)
            }
        }.cachedIn(viewModelScope).flowOn(defaultDispatcher)

    private val _snackbarEvent = MutableSharedFlow<UiText>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    fun toggleFavourite(album: AlbumUIModel) {
        viewModelScope.launch {
            repository.toggleFavourite(album.id, album.isFavourite)
            val message = if (!album.isFavourite) {
                UiText.StringResource(fr.leboncoin.resources.R.string.added_to_favourites)
            } else {
                UiText.StringResource(fr.leboncoin.resources.R.string.removed_from_favourites)
            }
            _snackbarEvent.emit(message)
        }
    }
}
