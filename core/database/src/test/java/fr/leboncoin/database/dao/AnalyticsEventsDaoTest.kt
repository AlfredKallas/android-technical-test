package fr.leboncoin.database.dao

import fr.leboncoin.database.model.AnalyticsEventsEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class AnalyticsEventsDaoTest : DatabaseTest() {

    @Test
    @Throws(Exception::class)
    fun `GIVEN an event WHEN inserting it THEN it should be in the database`() = runTest {
        // GIVEN
        val event = AnalyticsEventsEntity(1L, "IMPRESSION", "AlbumsList", 123L, "{}")

        // WHEN
        analyticsDao.insertEvent(event)

        // THEN
        val events = analyticsDao.getAllEvents()
        assertEquals(1, events.size)
        assertEquals(event, events[0])
    }

    @Test
    fun `GIVEN multiple events WHEN inserting them THEN they should all be in the database`() = runTest {
        // GIVEN
        val event1 = AnalyticsEventsEntity(1L, "IMPRESSION", "AlbumsList", 123L, "{}")
        val event2 = AnalyticsEventsEntity(2L, "CLICK", "AlbumsList", 456L, "{}")

        // WHEN
        analyticsDao.insertEvent(event1)
        analyticsDao.insertEvent(event2)

        // THEN
        val events = analyticsDao.getAllEvents()
        assertEquals(2, events.size)
        assertEquals(event1, events[0])
        assertEquals(event2, events[1])
    }

    @Test
    fun `GIVEN events in the database WHEN deleteAll THEN the database should be empty`() = runTest {
        // GIVEN
        val event1 = AnalyticsEventsEntity(1L, "IMPRESSION", "AlbumsList", 123L, "{}")
        analyticsDao.insertEvent(event1)
        assertEquals(1, analyticsDao.getAllEvents().size)

        // WHEN
        analyticsDao.deleteAll()

        // THEN
        assertEquals(0, analyticsDao.getAllEvents().size)
    }
}
