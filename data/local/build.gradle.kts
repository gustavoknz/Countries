plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.gustavo.countries.data.local"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))

    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    implementation(libs.google.gson)
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.junit4)
}
