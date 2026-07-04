package dev.gustavo.countries.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val jacocoVersion = catalog.findVersion("jacoco").get().requiredVersion

            configure<JacocoPluginExtension> {
                toolVersion = jacocoVersion
            }

            val extension = extensions.findByType(LibraryExtension::class.java)
                ?: extensions.findByType(ApplicationExtension::class.java)

            extension?.buildTypes?.getByName("debug")?.enableUnitTestCoverage = true

            tasks.withType<Test>().configureEach {
                jvmArgs("-Xshare:off")
            }

            tasks.withType<JacocoReport> {
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }
            }

            tasks.register<JacocoReport>("jacocoTestReport") {
                dependsOn("testDebugUnitTest")
                group = "Reporting"
                description = "Generate Jacoco coverage reports for the debug build."

                classDirectories.setFrom(getJacocoClassDirs())
                sourceDirectories.setFrom(getJacocoSourceDirs())
                executionData.setFrom(getJacocoExecutionData())
            }
        }
    }
}
