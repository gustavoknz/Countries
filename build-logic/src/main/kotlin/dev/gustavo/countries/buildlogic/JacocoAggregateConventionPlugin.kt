package dev.gustavo.countries.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoAggregateConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register<JacocoReport>("jacocoFullReport") {
                group = "Reporting"
                description = "Generates an aggregate Jacoco coverage report for all subprojects."

                val subprojectsTasks = subprojects.mapNotNull { it.tasks.findByName("jacocoTestReport") }
                dependsOn(subprojectsTasks)

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }

                classDirectories.setFrom(files(subprojects.map { it.getJacocoClassDirs() }))
                sourceDirectories.setFrom(files(subprojects.map { it.getJacocoSourceDirs() }))
                executionData.setFrom(files(subprojects.map { it.getJacocoExecutionData() }))
            }
        }
    }
}
