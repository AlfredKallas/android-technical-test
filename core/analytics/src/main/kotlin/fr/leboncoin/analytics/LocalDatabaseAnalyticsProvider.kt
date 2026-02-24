package fr.leboncoin.analytics

import fr.leboncoin.database.dao.AnalyticsEventsDao
import fr.leboncoin.database.model.AnalyticsEventsEntity
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LocalDatabaseAnalyticsProvider @Inject constructor(
    private val analyticsEventsDao: AnalyticsEventsDao,
    private val json: Json
):  AnalyticsProvider {
    override suspend fun logEvent(event: AnalyticsEvent) {
        analyticsEventsDao.insertEvent(event.toEntity(json))
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