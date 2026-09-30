package dev.gustavo.countries.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.findByType(LibraryExtension::class.java)
                ?: extensions.findByType(ApplicationExtension::class.java)

            extension?.buildFeatures?.compose = true

            configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
                        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                        "-opt-in=androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
                        "-opt-in=androidx.compose.ui.test.ExperimentalTestApi",
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-opt-in=kotlinx.coroutines.FlowPreview"
                    )
                    optIn.addAll(
                        "androidx.compose.animation.ExperimentalSharedTransitionApi",
                        "androidx.compose.material3.ExperimentalMaterial3Api",
                        "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
                        "androidx.compose.ui.test.ExperimentalTestApi"
                    )
                }
            }
        }
    }
}
