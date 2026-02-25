plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.art.android.library.flavors)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "fr.leboncoin.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }

    dependencies {
        api(projects.core.network)
        api(projects.core.database)
        api(projects.core.analytics)
        api(projects.core.common)

        implementation(libs.retrofit.core)
        implementation(libs.retrofit.kotlin.serialization)
        implementation(libs.okhttp.logging)

        implementation(libs.coil.network.okhttp)
        implementation(libs.coil.svg)

        //Hilt
        implementation(libs.hilt.android)
        ksp(libs.hilt.compiler)

        //Paging
        implementation(libs.room.paging)

        implementation(libs.kotlin.serialization.json)

        testImplementation(libs.junit)
        testImplementation(libs.mockito.kotlin)
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
        androidTestImplementation(libs.androidx.junit) // Useless dependency
        androidTestImplementation(libs.androidx.espresso.core) // Useless dependency
    }
}
