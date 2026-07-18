package dev.gustavo.countries.buildlogic

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
            extensions.configure<DetektExtension> {
                config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                allRules = false
                parallel = true
            }

            dependencies {
                "detektPlugins"(catalog.findLibrary("detekt-formatting").get())
                "detektPlugins"(project(":core:detekt-rules"))
            }
        }
    }
}
