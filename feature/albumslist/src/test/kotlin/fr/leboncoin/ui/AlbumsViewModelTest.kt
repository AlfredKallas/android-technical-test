package fr.leboncoin.ui

import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.AnalyticsEventsRepository
import fr.leboncoin.ui.mapper.AlbumUIMapper
import org.junit.Test
import org.mockito.kotlin.mock

class AlbumsViewModelTest {

    @Test
    fun constructor_isInitialized() {
        val repository: AlbumRepository = mock()
        val analyticsRepository: AnalyticsEventsRepository = mock()
        val mapper: AlbumUIMapper = mock()
        val vm = AlbumsViewModel(repository, analyticsRepository, mapper)
        // Basic verification that VM can be instantiated
    }
}
