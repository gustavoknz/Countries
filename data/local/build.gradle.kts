import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.android.built-in-kotlin")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

configure<LibraryExtension> {
    namespace = "dev.gustavo.countries.data.local"
    compileSdk = 37

    defaultConfig { minSdk = 23 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.google.gson)
    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)
}
