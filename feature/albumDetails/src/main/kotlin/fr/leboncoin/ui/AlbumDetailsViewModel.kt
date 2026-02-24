package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.data.repository.AlbumRepository
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {
    // Logic for loading album details
}