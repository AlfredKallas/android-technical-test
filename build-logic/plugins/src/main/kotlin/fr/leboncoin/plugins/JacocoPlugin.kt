package fr.leboncoin.plugins

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            extensions.configure<JacocoPluginExtension> {
                toolVersion = "0.8.12"
            }

            val androidComponents = extensions.findByType(LibraryAndroidComponentsExtension::class.java)

            tasks.withType<Test>().configureEach {
                configure<org.gradle.testing.jacoco.plugins.JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }

            val jacocoTestReport = tasks.register<JacocoReport>("jacocoTestReport") {
                dependsOn("testDebugUnitTest")

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }

                val fileFilter = listOf(
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    "**/*Module*.*",
                    "**/*Dagger*.*",
                    "**/*Hilt*.*",
                    "**/*_Factory*.*",
                    "**/*_MembersInjector*.*",
                    "**/*_Provides*.*",
                    "**/*_Component*.*",
                    "**/*Screen*.*",
                    "**/*Entry*.*",
                    "**/*MainActivity*.*",
                    "**/*Navigation*.*",
                    "**/*Navigator*.*"
                )

                val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
                    exclude(fileFilter)
                }
                val mainSrc = "${project.projectDir}/src/main/kotlin"

                sourceDirectories.setFrom(files(mainSrc))
                classDirectories.setFrom(files(debugTree))
                executionData.setFrom(fileTree(project.buildDir) {
                    include("jacoco/testDebugUnitTest.exec")
                })
            }
        }
    }
}
