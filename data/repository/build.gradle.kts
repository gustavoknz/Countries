plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.gustavo.countries.data.repository"

    kotlin {
        compilerOptions {
            optIn.add("androidx.paging.ExperimentalPagingApi")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":core:common"))

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.paging.common)
    implementation(libs.bundles.room)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.androidx.paging.testing)

    androidTestImplementation(libs.bundles.instrumented.test)
}
