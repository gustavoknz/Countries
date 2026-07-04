package dev.gustavo.countries.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }

            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "implementation"(catalog.findLibrary("hilt-android").get())
                "ksp"(catalog.findLibrary("hilt-compiler").get())
                
                // hilt-navigation-compose is commonly needed in features
                catalog.findLibrary("hilt-navigation-compose").ifPresent {
                    "implementation"(it)
                }
            }
        }
    }
}
