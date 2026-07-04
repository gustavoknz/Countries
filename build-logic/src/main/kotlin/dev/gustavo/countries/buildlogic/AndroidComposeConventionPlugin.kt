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

            extension?.apply {
                buildFeatures.compose = true
            }

            configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    optIn.addAll(
                        "androidx.compose.animation.ExperimentalSharedTransitionApi",
                        "androidx.compose.material3.ExperimentalMaterial3Api"
                    )
                }
            }
        }
    }
}
