package fr.leboncoin.data.repository

import fr.leboncoin.analytics.AnalyticsEvent
import fr.leboncoin.analytics.AnalyticsProvider
import fr.leboncoin.database.dao.AnalyticsEventsDao
import fr.leboncoin.database.model.AnalyticsEventsEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import java.lang.System
import javax.inject.Inject
import kotlin.Long

interface AnalyticsEventsRepository {
    suspend fun insertEvent(event: AnalyticsEvent)
    suspend fun logEvent(event: AnalyticsEvent)

}

class AnalyticsEventsRepositoryImpl @Inject constructor(
    private val providers: Set<@JvmSuppressWildcards AnalyticsProvider>,
    private val analyticsEventsDao: AnalyticsEventsDao,
    private val json: Json
): AnalyticsEventsRepository {
    override suspend fun insertEvent(event: AnalyticsEvent) {
        analyticsEventsDao.insertEvent(event.toEntity(json))
    }

    override suspend fun logEvent(event: AnalyticsEvent) = coroutineScope {
        providers.forEach {
            async {
                it.logEvent(event)
            }.await()
        }
    }
}

private fun AnalyticsEvent.toEntity(
    json: Json
): AnalyticsEventsEntity {
    return AnalyticsEventsEntity(
        eventName = type.name,
        screenName = type.analyticsAtScreen,
        timestamp = System.currentTimeMillis(),
        properties = json.encodeToString(extras)
    )
}