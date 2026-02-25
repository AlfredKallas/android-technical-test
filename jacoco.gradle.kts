apply(plugin = "jacoco")

configure<JacocoPluginExtension> {
    toolVersion = "0.8.12"
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

// Helper function to setup individual module reports
fun setupJacocoReportTask(task: JacocoReport, isAndroid: Boolean) {
    with(task) {
        group = "Reporting"
        description = "Generate Jacoco coverage reports"

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        classDirectories.setFrom(provider {
            val buildDirAsFile = layout.buildDirectory.get().asFile
            if (isAndroid) {
                val possibleKotlinDirs = listOf(
                    "intermediates/classes/debug/transformDebugClassesWithAsm/dirs",
                    "intermediates/runtime_library_classes_dir/debug/bundleLibRuntimeToDirDebug",
                    "intermediates/runtime_library_classes_dir/debug",
                    "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes",
                    "tmp/kotlin-classes/debug"
                )
                val chosenKotlinDir = possibleKotlinDirs.map { File(buildDirAsFile, it) }
                    .firstOrNull { it.exists() && it.walkTopDown().any { f -> f.extension == "class" } }

                val javacDir = File(buildDirAsFile, "intermediates/javac/debug/classes")

                val trees = mutableListOf<org.gradle.api.file.ConfigurableFileTree>()
                if (chosenKotlinDir != null) trees.add(fileTree(chosenKotlinDir) { exclude(fileFilter) })
                if (javacDir.exists()) trees.add(fileTree(javacDir) { exclude(fileFilter) })
                trees
            } else {
                listOf(fileTree("${buildDirAsFile}/classes/kotlin/main") { exclude(fileFilter) })
            }
        })

        val executionDataPath = if (isAndroid) {
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
        } else {
            "jacoco/test.exec"
        }
        
        val testTaskName = if (isAndroid) "testDebugUnitTest" else "test"
        tasks.findByName(testTaskName)?.let {
            dependsOn(it)
        }

        sourceDirectories.setFrom(files("${project.projectDir}/src/main/kotlin", "${project.projectDir}/src/main/java"))
        executionData.setFrom(fileTree(layout.buildDirectory.get()) {
            include(executionDataPath)
        })
    }
}

// Logic for individual modules
if (project != rootProject) {
    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    val taskName = "jacocoTestReport"

    tasks.withType<JacocoReport>().configureEach {
        if (name == taskName) {
            val isAndroid = project.plugins.hasPlugin("com.android.library") || 
                           project.plugins.hasPlugin("com.android.application")
            setupJacocoReportTask(this, isAndroid)
        }
    }

    afterEvaluate {
        val isAndroid = project.plugins.hasPlugin("com.android.library") || 
                       project.plugins.hasPlugin("com.android.application")
        val testTaskName = if (isAndroid) "testDebugUnitTest" else "test"
        
        if (tasks.findByName(testTaskName) != null) {
            if (tasks.findByName(taskName) == null) {
                tasks.register<JacocoReport>(taskName) {
                    setupJacocoReportTask(this, isAndroid)
                }
            }
        }
    }
} else {
    // Logic for Root Project Aggregation
    val mergedTask = tasks.register<JacocoReport>("jacocoMergedReport") {
        group = "Reporting"
        description = "Generate Merged Jacoco coverage report for all modules"

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    subprojects.forEach { subproject ->
        // Helper function for subproject configuration
        fun configureSubproject(isAndroidSub: Boolean) {
            val testTaskName = if (isAndroidSub) "testDebugUnitTest" else "test"
            
            mergedTask.configure {
                classDirectories.from(subproject.provider {
                    val buildDirAsFile = subproject.layout.buildDirectory.get().asFile
                    if (isAndroidSub) {
                        val possibleKotlinDirs = listOf(
                            "intermediates/classes/debug/transformDebugClassesWithAsm/dirs",
                            "intermediates/runtime_library_classes_dir/debug/bundleLibRuntimeToDirDebug",
                            "intermediates/runtime_library_classes_dir/debug",
                            "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes",
                            "tmp/kotlin-classes/debug"
                        )
                        val chosenKotlinDir = possibleKotlinDirs.map { File(buildDirAsFile, it) }
                            .firstOrNull { it.exists() && it.walkTopDown().any { f -> f.extension == "class" } }

                        val javacDir = File(buildDirAsFile, "intermediates/javac/debug/classes")

                        val trees = mutableListOf<org.gradle.api.file.ConfigurableFileTree>()
                        if (chosenKotlinDir != null) trees.add(subproject.fileTree(chosenKotlinDir) { exclude(fileFilter) })
                        if (javacDir.exists()) trees.add(subproject.fileTree(javacDir) { exclude(fileFilter) })
                        trees
                    } else {
                        listOf(subproject.fileTree("${buildDirAsFile}/classes/kotlin/main") { exclude(fileFilter) })
                    }
                })

                val executionDataPath = if (isAndroidSub) {
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
                } else {
                    "jacoco/test.exec"
                }

                dependsOn("${subproject.path}:$testTaskName")

                sourceDirectories.from(files("${subproject.projectDir}/src/main/kotlin", "${subproject.projectDir}/src/main/java"))
                executionData.from(fileTree(subproject.layout.buildDirectory.get()) {
                    include(executionDataPath)
                })
            }
        }

        // Use pluginManager.withPlugin to avoid type mismatch with PluginContainer.withId
        subproject.pluginManager.withPlugin("com.android.library") { configureSubproject(true) }
        subproject.pluginManager.withPlugin("com.android.application") { configureSubproject(true) }
        subproject.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { configureSubproject(false) }
    }
}
