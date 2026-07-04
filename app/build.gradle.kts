plugins {
    id("countries.android.application")
    id("countries.android.compose")
    id("countries.android.hilt")
    id("countries.jacoco")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.gustavo.countries"

    defaultConfig {
        applicationId = "dev.gustavo.countries"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugaring)

    implementation(project(":feature:detail"))
    implementation(project(":feature:list"))
    implementation(project(":domain"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":data:repository"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.lifecycle)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.google.material)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.test.androidx.compose.junit4)
    androidTestImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.test.android.mockk)
}
