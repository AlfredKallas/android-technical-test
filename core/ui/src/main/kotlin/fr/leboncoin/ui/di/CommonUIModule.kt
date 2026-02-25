package fr.leboncoin.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.mapper.AlbumUIMapperImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonUIModule {

    @Provides
    @Singleton
    fun provideAlbumUIMapper(): AlbumUIMapper {
        return AlbumUIMapperImpl()
    }

}