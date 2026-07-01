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
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.google.gson)
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(libs.bundles.instrumented.test)
}
