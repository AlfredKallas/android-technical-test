package fr.leboncoin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.leboncoin.database.model.AnalyticsEventsEntity

@Dao
interface AnalyticsEventsDao {
    @Insert
    suspend fun insertEvent(event: AnalyticsEventsEntity)

    @Query("SELECT * FROM analytics_events")
    suspend fun getAllEvents(): List<AnalyticsEventsEntity>

    @Query("DELETE FROM analytics_events")
    suspend fun deleteAll()
}
