package fr.leboncoin.androidrecruitmenttestapp

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependencies
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import javax.inject.Inject

@HiltAndroidApp
class PhotoApp : Application(), AppDependenciesProvider, SingletonImageLoader.Factory {

    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()

    override val dependencies: AppDependencies by lazy { AppDependencies() }
}