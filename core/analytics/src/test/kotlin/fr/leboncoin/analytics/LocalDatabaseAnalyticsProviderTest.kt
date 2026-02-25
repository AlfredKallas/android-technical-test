package fr.leboncoin.analytics

import fr.leboncoin.database.dao.AnalyticsEventsDao
import fr.leboncoin.database.model.AnalyticsEventsEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class LocalDatabaseAnalyticsProviderTest {

    private lateinit var analyticsProvider: LocalDatabaseAnalyticsProvider
    private val analyticsEventsDao: AnalyticsEventsDao = mock()
    private val json = Json

    @Before
    fun setup() {
        analyticsProvider = LocalDatabaseAnalyticsProvider(analyticsEventsDao, json)
    }

    @Test
    fun `WHEN logEvent is called THEN insertEvent is called on the dao`() = runTest {
        // GIVEN
        val event = AnalyticsEvent(AnalyticsEvent.AnalyticsType.ScreenView("AlbumsList"))

        // WHEN
        analyticsProvider.logEvent(event)

        // THEN
        verify(analyticsEventsDao).insertEvent(any<AnalyticsEventsEntity>())
    }
}
