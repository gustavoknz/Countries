import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("jacoco")
}

val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val compileSdkVersion = catalog.findVersion("compileSdk").get().requiredVersion.toInt()
val minSdkVersion = catalog.findVersion("minSdk").get().requiredVersion.toInt()
val targetSdkVersion = catalog.findVersion("targetSdk").get().requiredVersion.toInt()
val javaV = JavaVersion.toVersion(catalog.findVersion("java").get().requiredVersion)
val jvmV = JvmTarget.fromTarget(catalog.findVersion("java").get().requiredVersion)
val jacocoVersion = catalog.findVersion("jacoco").get().requiredVersion

val jacocoExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    "**/*_Hilt*.class",
    "**/Hilt_*.class",
    "**/*_Factory.class",
    "**/*_MembersInjector.class",
    "**/*Module_*Factory.class",
    "**/*_Impl*.class",
    "**/*_Table*.class",
    "**/*ComposableSingletons$*.*",
    "**/*$*preview$*.*",
    "**/*$*Preview$*.*",
    "**/*Screen*.*",
    "**/*Route*.*",
    "**/*Action*.*",
    "**/*Event*.*",
    "**/*ViewState*.*",
    "**/*Theme*.*",
    "**/*Color*.*",
    "**/*Dimens*.*",
    "**/*UiText*.*",
    "**/*Kt.class",
    "**/di/**",
    "**/model/**",
    "**/entities/**",
    "**/entity/**",
    "**/dto/**",
    "**/*DTO*.*",
    "**/MainActivity.class",
    "**/CountriesApplication.class",
    "**/*Repository.class",
    "**/*ApiService.class",
    $$"**/*$DefaultImpls.class",
    "**/DefaultDispatcherProvider.class",
    "**/CountriesDatabase.class"
)

fun Project.getJacocoClassDirs(): FileCollection {
    val javaClasses = fileTree("${project.layout.buildDirectory.get()}/intermediates/javac/debug/classes") {
        exclude(jacocoExclusions)
    }
    val kotlinClasses = fileTree("${project.layout.buildDirectory.get()}/intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes") {
        exclude(jacocoExclusions)
    }
    // Support for pure Kotlin modules (if added in the future)
    val pureKotlinClasses = fileTree("${project.layout.buildDirectory.get()}/classes/kotlin/main") {
        exclude(jacocoExclusions)
    }
    return files(javaClasses, kotlinClasses, pureKotlinClasses)
}

fun Project.getJacocoSourceDirs(): FileCollection {
    return files("${project.projectDir}/src/main/java", "${project.projectDir}/src/main/kotlin")
}

fun Project.getJacocoExecutionData(): FileCollection {
    return fileTree(project.layout.buildDirectory) {
        include(
            "jacoco/testDebugUnitTest.exec",
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "jacoco/test.exec" // For pure Kotlin modules
        )
    }
}

subprojects {
    pluginManager.apply("jacoco")

    configure<JacocoPluginExtension> {
        toolVersion = jacocoVersion
    }

    pluginManager.withPlugin("com.android.library") {
        configure<LibraryExtension> {
            compileSdk = compileSdkVersion
            defaultConfig {
                minSdk = minSdkVersion
            }
            compileOptions {
                sourceCompatibility = javaV
                targetCompatibility = javaV
            }
            buildTypes {
                getByName("debug") {
                    enableUnitTestCoverage = true
                }
            }
        }
    }

    pluginManager.withPlugin("com.android.application") {
        configure<ApplicationExtension> {
            compileSdk = compileSdkVersion
            defaultConfig {
                minSdk = minSdkVersion
                targetSdk = targetSdkVersion
            }
            compileOptions {
                sourceCompatibility = javaV
                targetCompatibility = javaV
            }
            buildTypes {
                getByName("debug") {
                    enableUnitTestCoverage = true
                }
            }
        }
    }

    // Since AGP 9.0+, Kotlin support is built-in.
    // We configure Kotlin options when any Android plugin is applied.
    pluginManager.withPlugin("com.android.base") {
        configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(jvmV)
                optIn.addAll(
                    "kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "kotlinx.coroutines.FlowPreview"
                )
            }
        }
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") {
        configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                optIn.addAll(
                    "androidx.compose.animation.ExperimentalSharedTransitionApi",
                    "androidx.compose.material3.ExperimentalMaterial3Api"
                )
            }
        }
    }

    tasks.withType<Test>().configureEach {
        jvmArgs("-Xshare:off")
    }

    tasks.withType<JavaExec>().configureEach {
        jvmArgs("-Xshare:off")
    }

    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    pluginManager.withPlugin("com.android.base") {
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
