/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.leboncoin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.leboncoin.database.util.InstantConverter
import fr.leboncoin.database.dao.AnalyticsEventsDao
import fr.leboncoin.database.model.AnalyticsEventsEntity

@Database(
    entities = [
        AnalyticsEventsEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class LeboncoinDatabase : RoomDatabase() {
    abstract fun analyticsEventsDao(): AnalyticsEventsDao
}
