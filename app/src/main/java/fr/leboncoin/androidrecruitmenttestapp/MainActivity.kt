package fr.leboncoin.androidrecruitmenttestapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.adevinta.spark.SparkTheme
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.AlbumsScreen
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AlbumsViewModel by viewModels()

    private val analyticsHelper: AnalyticsHelper by lazy {
        val dependencies = (application as AppDependenciesProvider).dependencies
        dependencies.analyticsHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analyticsHelper.initialize(this)

        setContent {
            SparkTheme {
                AlbumsScreen(
                    viewModel = viewModel,
                    onItemSelected = {
                        analyticsHelper.trackSelection(it.id.toString())
                        startActivity(Intent(this, DetailsActivity::class.java))
                    }
                )
            }
        }
    }
}