import java.util.Properties

plugins {
    id("countries.android.library")
    id("countries.android.hilt")
    id("countries.jacoco")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.gustavo.countries.data.remote"

    buildFeatures {
        buildConfig = true
    }

    val localProperties = Properties().apply {
        val propertiesFile = rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { load(it) }
        }
    }
    val apiKey = localProperties.getProperty("REST_COUNTRIES_API_KEY") ?: ""

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://api.restcountries.com/\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))

    implementation(libs.bundles.retrofit)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(libs.bundles.instrumented.test)
}
