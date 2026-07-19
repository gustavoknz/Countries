package dev.gustavo.countries.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

class ProjectGraphConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("generateModuleGraph", GenerateModuleGraphTask::class.java) {
            outputFile.set(target.file("MODULE_GRAPH.md"))
            
            val projectNames = target.rootProject.subprojects.associate { it.name to it.path }
            
            mermaidEdges.set(target.provider {
                val edges = mutableListOf<String>()
                val seenEdges = mutableSetOf<String>()
                
                target.rootProject.subprojects.forEach { subproject ->
                    val sourcePath = subproject.path
                    if (sourcePath.startsWith(":build-logic")) return@forEach
                    val sourceId = sourcePath.replace(":", "_")

                    subproject.configurations.forEach { config ->
                        val configName = config.name
                        if (configName !in listOf("implementation", "api", "ksp", "testImplementation", "androidTestImplementation") &&
                            !configName.endsWith("Implementation") && !configName.endsWith("Api")) {
                            return@forEach
                        }

                        config.dependencies.forEach { dependency ->
                            val targetPath = projectNames[dependency.name]
                            if (targetPath != null && targetPath != sourcePath) {
                                val targetId = targetPath.replace(":", "_")
                                val arrow = when {
                                    configName.contains("test", ignoreCase = true) -> "-.->"
                                    configName.contains("api", ignoreCase = true) -> "==>"
                                    else -> "-->"
                                }
                                
                                val edge = "  $sourceId(\"$sourcePath\") $arrow $targetId(\"$targetPath\")"
                                if (seenEdges.add(edge)) {
                                    edges.add(edge)
                                }
                            }
                        }
                    }
                }
                edges
            })
        }
    }
}

abstract class GenerateModuleGraphTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: Property<File>

    @get:Input
    abstract val mermaidEdges: ListProperty<String>

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

        mermaidEdges.get().forEach { edge ->
            builder.append(edge).append("\n")
        }

        builder.append("```\n")
        
        val file = outputFile.get()
        file.writeText(builder.toString())
        
        logger.lifecycle("Module graph updated. Found ${mermaidEdges.get().size} dependencies.")
    }
}
