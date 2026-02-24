package fr.leboncoin.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analytics_events")
data class AnalyticsEventsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventName: String,
    val screenName: String,
    val timestamp: Long,
    val properties: String // JSON string of details
)