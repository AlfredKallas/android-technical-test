package fr.leboncoin.androidrecruitmenttestapp.di

import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import java.util.logging.Logger

interface AppDependenciesProvider {
    val dependencies: AppDependencies
}

class AppDependencies {
    val logger: Logger by lazy { Logger.getGlobal() }
}