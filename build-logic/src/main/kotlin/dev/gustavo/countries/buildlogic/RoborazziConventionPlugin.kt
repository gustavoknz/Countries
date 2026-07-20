package dev.gustavo.countries.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class RoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.takahirom.roborazzi")

            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
            dependencies {
                "testImplementation"(catalog.findLibrary("roborazzi-core").get())
                "testImplementation"(catalog.findLibrary("roborazzi-compose").get())
                "testImplementation"(catalog.findLibrary("roborazzi-junit").get())
                "testImplementation"("org.robolectric:robolectric:4.12.2")
            }

            tasks.withType<Test>().configureEach {
                // Robolectric configurations
                systemProperty("robolectric.graphicsMode", "NATIVE")
                
                // Roborazzi configurations
                // Use src/test/screenshots as the baseline directory
                systemProperty("roborazzi.output.dir", project.file("src/test/screenshots").absolutePath)
                
                // Optimization: record only when explicitly requested via command line
                val isRecordRun = project.hasProperty("roborazzi.test.record") || 
                                 project.gradle.startParameter.taskNames.any { it.contains("recordRoborazzi", ignoreCase = true) }
                
                if (isRecordRun) {
                    systemProperty("roborazzi.test.record", "true")
                }
            }
        }
    }
}
