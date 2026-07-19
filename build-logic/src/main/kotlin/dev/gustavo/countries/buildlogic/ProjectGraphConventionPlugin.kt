package dev.gustavo.countries.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

class ProjectGraphConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("generateModuleGraph", GenerateModuleGraphTask::class.java) {
            outputFile.set(target.file("MODULE_GRAPH.md"))
        }
    }
}

abstract class GenerateModuleGraphTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: Property<File>

    init {
        group = "Documentation"
        description = "Generates a Mermaid-based module dependency graph."
    }

    @TaskAction
    fun generate() {
        val builder = StringBuilder()
        builder.append("# Module Dependency Graph\n\n")
        builder.append("```mermaid\n")
        builder.append("graph TD\n")

        val seenEdges = mutableSetOf<String>()
        val allSubprojects = project.rootProject.subprojects
        val projectNames = allSubprojects.associate { it.name to it.path }

        allSubprojects.forEach { subproject ->
            val sourcePath = subproject.path
            if (sourcePath.startsWith(":build-logic")) return@forEach

            subproject.configurations.forEach { config ->
                // Filter common configurations to reduce noise
                val configName = config.name
                if (configName !in listOf("implementation", "api", "ksp", "testImplementation", "androidTestImplementation") &&
                    !configName.endsWith("Implementation") && !configName.endsWith("Api")) {
                    return@forEach
                }

                config.dependencies.forEach { dependency ->
                    val targetPath = projectNames[dependency.name]
                    
                    if (targetPath != null && targetPath != sourcePath) {
                        val arrow = when {
                            configName.contains("test", ignoreCase = true) -> "-.->"
                            configName.contains("api", ignoreCase = true) -> "==>"
                            else -> "-->"
                        }
                        
                        val edge = "  \"$sourcePath\" $arrow \"$targetPath\""
                        if (seenEdges.add(edge)) {
                            builder.append("$edge\n")
                        }
                    }
                }
            }
        }

        builder.append("```\n")
        outputFile.get().writeText(builder.toString())
        logger.lifecycle("Module graph updated. Found ${seenEdges.size} dependencies.")
    }
}
