package fr.leboncoin.data.repository

import fr.leboncoin.analytics.AnalyticsEvent
import fr.leboncoin.analytics.AnalyticsProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsEventsRepositoryTest {

    private val provider1: AnalyticsProvider = mock()
    private val provider2: AnalyticsProvider = mock()
    private val providers = setOf(provider1, provider2)

    @Test
    fun `logEvent calls all providers`() = runTest {
        val repository = AnalyticsEventsRepositoryImpl(
            providers = providers,
            externalScope = this
        )
        val event = AnalyticsEvent(
            type = AnalyticsEvent.AnalyticsType.UserInteraction("TestScreen"),
            extras = emptyList()
        )

        repository.logEvent(event)
        advanceUntilIdle()

        verify(provider1).logEvent(event)
        verify(provider2).logEvent(event)
    }
}
