package dev.gustavo.countries.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoAggregateConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val jacocoVersion = catalog.findVersion("jacoco").get().requiredVersion

            configure<JacocoPluginExtension> {
                toolVersion = jacocoVersion
            }

            tasks.register<JacocoReport>("jacocoFullReport") {
                group = "Reporting"
                description = "Generates an aggregate Jacoco coverage report for all subprojects."

                val reportableProjects = subprojects.filter { 
                    it.pluginManager.hasPlugin("jacoco")
                }

                val subprojectsTasks = reportableProjects.mapNotNull { it.tasks.findByName("jacocoTestReport") }
                dependsOn(subprojectsTasks)

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/full/html"))
                    xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/full/jacocoFullReport.xml"))
                }

                classDirectories.setFrom(files(reportableProjects.map { it.getJacocoClassDirs() }))
                sourceDirectories.setFrom(files(reportableProjects.map { it.getJacocoSourceDirs() }))
                executionData.setFrom(files(reportableProjects.map { it.getJacocoExecutionData() }))
            }
        }
    }
}
