package fr.leboncoin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.analytics.AnalyticsEvent
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.AnalyticsEventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val analyticsRepository: AnalyticsEventsRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<LCResult<Unit>>(LCResult.Loading)
    val syncState: StateFlow<LCResult<Unit>> = _syncState.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            repository.sync().collect {
                _syncState.value = it
            }
        }
    }

    val paginationFlow = repository.getAlbums()
        .cachedIn(viewModelScope)

    fun trackEventOnItemSelected(id: String) {
        analyticsRepository.logEvent(
            AnalyticsEvent(
                AnalyticsEvent.AnalyticsType.UserInteraction("AlbumsScreen"),
                listOf(AnalyticsEvent.Param("item_id", id))
            )
        )
    }


}