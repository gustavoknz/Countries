import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
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
}
