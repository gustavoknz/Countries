package dev.gustavo.countries.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("countries.ktlint")
                apply("countries.detekt")
            }

            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val targetSdkVersion = catalog.findVersion("targetSdk").get().requiredVersion.toInt()

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = targetSdkVersion
            }
        }
    }
}
