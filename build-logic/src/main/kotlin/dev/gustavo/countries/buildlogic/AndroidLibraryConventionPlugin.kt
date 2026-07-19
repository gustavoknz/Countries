package dev.gustavo.countries.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("countries.ktlint")
                apply("countries.detekt")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                
                @Suppress("UnstableApiUsage")
                testOptions {
                    unitTests.isReturnDefaultValues = true
                }

                defaultConfig {
                    val proguardFile = "consumer-rules.pro"
                    if (file(proguardFile).exists()) {
                        consumerProguardFiles(proguardFile)
                    }
                }
            }
        }
    }
}
