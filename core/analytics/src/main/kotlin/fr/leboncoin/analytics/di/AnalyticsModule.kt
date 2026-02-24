package fr.leboncoin.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.analytics.AnalyticsHelper
import fr.leboncoin.analytics.StubAnalyticsHelper

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: StubAnalyticsHelper): AnalyticsHelper
}