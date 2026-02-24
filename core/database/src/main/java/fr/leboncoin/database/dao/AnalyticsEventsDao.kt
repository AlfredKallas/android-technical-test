package fr.leboncoin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import fr.leboncoin.database.model.AnalyticsEventsEntity

@Dao
interface AnalyticsEventsDao {
    @Insert
    suspend fun insertEvent(event: AnalyticsEventsEntity)
}