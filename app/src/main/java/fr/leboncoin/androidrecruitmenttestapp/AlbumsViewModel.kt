package fr.leboncoin.androidrecruitmenttestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.network.model.AlbumDto
import fr.leboncoin.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: AlbumRepository,
) : ViewModel() {

    private val _albums = MutableSharedFlow<List<AlbumDto>>()
    val albums: SharedFlow<List<AlbumDto>> = _albums

    fun loadAlbums() {
        viewModelScope.launch {
            try {
                _albums.emit(repository.getAllAlbums())
            } catch (ex: Exception) {
            println(ex)
            /* TODO: Handle errors */ }
        }
    }

    class Factory(
        private val repository: AlbumRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlbumsViewModel(repository) as T
        }
    }
}