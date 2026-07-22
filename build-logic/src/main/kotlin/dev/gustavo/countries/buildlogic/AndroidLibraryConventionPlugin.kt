package dev.gustavo.countries.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

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
                    unitTests {
                        isReturnDefaultValues = true
                        isIncludeAndroidResources = true
                    }
                }

                defaultConfig {
                    val proguardFile = "consumer-rules.pro"
                    if (file(proguardFile).exists()) {
                        consumerProguardFiles(proguardFile)
                    }
                }
            }

            tasks.withType<Test>().configureEach {
                // Modules that only contain test helpers might have no tests to run.
                // This prevents the task from failing when no tests are discovered.
                filter.isFailOnNoMatchingTests = false
                
                // Use setProperty to be compatible with different Gradle/AGP versions if direct property access is tricky
                try {
                    val method = this.javaClass.getMethod("setFailOnNoDiscoveredTests", Boolean::class.javaPrimitiveType)
                    method.invoke(this, false)
                } catch (_: NoSuchMethodException) {
                    // Property might not exist in some versions
                }
            }
        }
    }
}
