pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("fr\\.android.*")
                includeGroupByRegex("fr\\.leboncoin.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AndroidRecruitmentTestApp"
include(":app")
include(":core:data")
include(":core:network")
include(":core:analytics")
include(":core:database")
include(":core:common")
include(":core:ui")
include(":feature:albumslist")
include(":feature:albumDetails")
include(":feature:favourites")
include(":resources")
