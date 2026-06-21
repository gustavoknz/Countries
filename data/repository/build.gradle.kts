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

    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.junit4)
    testImplementation(libs.tests.kotlinx.coroutines.test)
    testImplementation(libs.tests.mockk)
    testImplementation(libs.androidx.paging.testing)
}
