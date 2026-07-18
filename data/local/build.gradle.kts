plugins {
    id("countries.android.library")
    id("countries.android.hilt")
    id("countries.jacoco")
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
    kspAndroidTest(libs.androidx.room.compiler)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(libs.bundles.instrumented.test)
}
