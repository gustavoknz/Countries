import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
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

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logginginterceptor)
    implementation(libs.google.gson)
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.tests.junit5)
    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.kotlinx.coroutines.test)
}
