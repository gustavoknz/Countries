package dev.gustavo.countries.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

class ProjectGraphConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("generateModuleGraph", GenerateModuleGraphTask::class.java) {
            outputFile.set(target.layout.projectDirectory.file("MODULE_GRAPH.md").asFile)

            val projectNodes = mutableMapOf<String, String>()
            val edges = mutableListOf<String>()
            val groups = mutableMapOf<String, MutableList<String>>()

            target.rootProject.allprojects.forEach { subproject ->
                val sourcePath = subproject.path
                if (sourcePath == ":" || sourcePath.startsWith(":build-logic")) return@forEach
                
                // Only include modules with a build file
                if (!subproject.buildFile.exists()) return@forEach

                val sourceId = sourcePath.replace(":", "_")
                projectNodes[sourceId] = sourcePath

                // Handle grouping
                val parts = sourcePath.split(":").filter { it.isNotEmpty() }
                if (parts.size > 1) {
                    val groupName = parts[0]
                    if (groupName in listOf("core", "data", "feature")) {
                        groups.getOrPut(groupName) { mutableListOf() }.add(sourceId)
                    }
                }

                subproject.configurations.all {
                    val configName = this.name
                    if (configName in listOf("implementation", "api", "ksp", "testImplementation", "androidTestImplementation", "baselineProfile") ||
                        configName.endsWith("Implementation") || configName.endsWith("Api")) {

                        dependencies.all {
                            val depString = this.toString()
                            if (depString.startsWith("project '")) {
                                val targetPath = depString.substringAfter("'").substringBefore("'")
                                if (targetPath != sourcePath) {
                                    val targetId = targetPath.replace(":", "_")
                                    val arrow = when {
                                        configName.contains("test", ignoreCase = true) -> "-.->"
                                        configName.contains("api", ignoreCase = true) -> "==>"
                                        else -> "-->"
                                    }
                                    edges.add("  $sourceId $arrow $targetId")
                                }
                            }
                        }
                    }
                }
            }
            this.modules.set(projectNodes)
            this.mermaidEdges.set(edges)
            this.moduleGroups.set(groups.mapValues { it.value.toList() })
        }
    }
}

abstract class GenerateModuleGraphTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: Property<File>

    @get:Input
    abstract val modules: MapProperty<String, String>

    @get:Input
    abstract val mermaidEdges: ListProperty<String>

    @get:Input
    abstract val moduleGroups: MapProperty<String, List<String>>

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

        val groupedIds = moduleGroups.get().values.flatten().toSet()

        // Render top-level modules
        modules.get().forEach { (id, path) ->
            if (id !in groupedIds) {
                builder.append("  $id(\"$path\")\n")
            }
        }

        // Render groups
        moduleGroups.get().forEach { (groupName, ids) ->
            builder.append("\n  subgraph $groupName\n")
            ids.forEach { id ->
                val path = modules.get()[id]
                builder.append("    $id(\"$path\")\n")
            }
            builder.append("  end\n")
        }

        builder.append("\n")

        mermaidEdges.get().distinct().forEach { edge ->
            builder.append(edge).append("\n")
        }

        builder.append("```\n")

        val file = outputFile.get()
        file.writeText(builder.toString())

        logger.lifecycle("Module graph updated at: ${file.absolutePath}")
    }
}
