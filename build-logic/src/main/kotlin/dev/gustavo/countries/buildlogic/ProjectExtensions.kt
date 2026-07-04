package dev.gustavo.countries.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val compileSdkVersion = catalog.findVersion("compileSdk").get().requiredVersion.toInt()
    val minSdkVersion = catalog.findVersion("minSdk").get().requiredVersion.toInt()
    val javaVersion = JavaVersion.toVersion(catalog.findVersion("java").get().requiredVersion)
    val jvmTarget = JvmTarget.fromTarget(catalog.findVersion("java").get().requiredVersion)

    commonExtension.compileSdk = compileSdkVersion
    
    // Direct property access to avoid lambda dispatch issues in pre-compiled plugins
    commonExtension.defaultConfig.minSdk = minSdkVersion
    
    commonExtension.compileOptions.apply {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    @Suppress("UnstableApiUsage")
    commonExtension.testOptions.unitTests.isReturnDefaultValues = true

    configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            this.jvmTarget.set(jvmTarget)
            optIn.addAll(
                "kotlinx.coroutines.ExperimentalCoroutinesApi",
                "kotlinx.coroutines.FlowPreview"
            )
        }
    }
}

private val jacocoExclusions = listOf(
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
    "**/DefaultDispatcherProvider.class",
    "**/CountriesDatabase.class",
    "**/*\$*\$*.*",
    "**/*\$Continuation\$*.*",
    "**/*\$DefaultImpls.class",
    "**/*\$SAM\$*.*"
)

internal fun Project.getJacocoClassDirs(): ConfigurableFileCollection = files(
    fileTree("${layout.buildDirectory.get()}/intermediates/javac/debug/classes") {
        exclude(jacocoExclusions)
    },
    fileTree("${layout.buildDirectory.get()}/intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes") {
        exclude(jacocoExclusions)
    },
    fileTree("${layout.buildDirectory.get()}/classes/kotlin/main") {
        exclude(jacocoExclusions)
    }
)

internal fun Project.getJacocoSourceDirs(): ConfigurableFileCollection = files(
    "${projectDir}/src/main/java",
    "${projectDir}/src/main/kotlin"
)

internal fun Project.getJacocoExecutionData(): ConfigurableFileTree = fileTree(layout.buildDirectory) {
    include(
        "jacoco/testDebugUnitTest.exec",
        "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
        "jacoco/test.exec"
    )
}
