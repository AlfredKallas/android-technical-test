package fr.leboncoin.androidrecruitmenttestapp

import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.AnalyticsEventsRepository
import org.junit.Test
import org.mockito.kotlin.mock

class AlbumsViewModelTest {

    @Test
    fun constructor_isInitialized() {
        val repository: AlbumRepository = mock()
        val analyticsRepository: AnalyticsEventsRepository = mock()
        val vm = AlbumsViewModel(repository, analyticsRepository)
        // Basic verification that VM can be instantiated
    }
}

