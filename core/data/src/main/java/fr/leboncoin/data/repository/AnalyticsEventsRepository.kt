package fr.leboncoin.data.repository

import com.google.samples.apps.nowinandroid.core.common.network.di.ApplicationScope
import fr.leboncoin.analytics.AnalyticsEvent
import fr.leboncoin.analytics.AnalyticsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AnalyticsEventsRepository {
    fun logEvent(event: AnalyticsEvent)

}

class AnalyticsEventsRepositoryImpl @Inject constructor(
    private val providers: Set<@JvmSuppressWildcards AnalyticsProvider>,
    // This is added to not stop logging an event if it takes a bit longer
    // and the user has moved away from the screen
    @ApplicationScope private val externalScope: CoroutineScope,
): AnalyticsEventsRepository {

    override fun logEvent(event: AnalyticsEvent) {
        externalScope.launch {
            providers.forEach {
                launch {
                    it.logEvent(event)
                }
            }
        }
    }
}