package dev.gustavo.countries.buildlogic

import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
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

            extensions.configure<RoborazziExtension> {
                outputDir.set(project.file("src/test/screenshots"))
                @Suppress("OPT_IN_USAGE")
                @OptIn(com.github.takahirom.roborazzi.ExperimentalRoborazziApi::class)
                separateOutputDirs.set(true)
            }

            tasks.withType<Test>().configureEach {
                // Robolectric configurations
                systemProperty("robolectric.graphicsMode", "NATIVE")
            }
        }
    }
}
