plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.dependency.analysis)
}

apply(from = "jacoco.gradle.kts")

subprojects {
    apply(plugin = "com.autonomousapps.dependency-analysis")
    
    // Apply JaCoCo only to modules that are not just resource modules
    if (project.name != "resources") {
        apply(from = "$rootDir/jacoco.gradle.kts")
    }
}
