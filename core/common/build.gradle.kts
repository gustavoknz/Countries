plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.gustavo.countries.core.common"
    compileSdk = 36

    defaultConfig { minSdk = 23 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    // @Inject annotation — javax.inject is bundled with hilt/dagger but
    // we only need the annotation here, so we use the standalone artifact.
    implementation("javax.inject:javax.inject:1")
}
