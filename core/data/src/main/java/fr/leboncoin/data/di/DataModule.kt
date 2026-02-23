package fr.leboncoin.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.OfflineFirstAlbumRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsAlbumRepository(
        offlineFirstAlbumRepository: OfflineFirstAlbumRepository,
    ): AlbumRepository
}
