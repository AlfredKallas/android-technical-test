package fr.leboncoin.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import fr.leboncoin.analytics.AnalyticsProvider
import fr.leboncoin.analytics.LocalDatabaseAnalyticsProvider
import fr.leboncoin.analytics.StubAnalyticsProvider

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    @Binds
    @IntoSet
    abstract fun bindsAnalyticsProvider(analyticsHelperImpl: StubAnalyticsProvider): AnalyticsProvider


    @Binds
    @IntoSet
    abstract fun bindsDatabaseAnalyticsProvider(analyticsHelperImpl: LocalDatabaseAnalyticsProvider): AnalyticsProvider
}