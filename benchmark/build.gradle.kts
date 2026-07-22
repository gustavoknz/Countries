plugins {
    id("com.android.test")
    id("androidx.baselineprofile")
}

android {
    namespace = "dev.gustavo.countries.benchmark"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        targetSdk = 37

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    buildTypes {
        register("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // With AGP 9.0+ built-in Kotlin support, we use the android extension to configure Kotlin
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.test.androidx.junit.ext)
    implementation(libs.test.androidx.test.runner)
}

baselineProfile {
    useConnectedDevices = true
}
