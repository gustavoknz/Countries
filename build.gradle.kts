import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    id("com.android.built-in-kotlin") version "9.2.1" apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val compileSdkVersion = catalog.findVersion("compileSdk").get().requiredVersion.toInt()
val minSdkVersion = catalog.findVersion("minSdk").get().requiredVersion.toInt()
val targetSdkVersion = catalog.findVersion("targetSdk").get().requiredVersion.toInt()
val javaV = JavaVersion.toVersion(catalog.findVersion("javaVersion").get().requiredVersion)
val jvmV = JvmTarget.fromTarget(catalog.findVersion("jvmTarget").get().requiredVersion)

subprojects {
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
        }
    }

    pluginManager.withPlugin("com.android.built-in-kotlin") {
        configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(jvmV)
            }
        }
    }
}
