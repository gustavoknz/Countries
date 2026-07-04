import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `kotlin-dsl`
}

group = "dev.gustavo.countries.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    compileOnly(catalog.findLibrary("android-gradlePlugin").get())
    compileOnly(catalog.findLibrary("kotlin-gradlePlugin").get())
    compileOnly(catalog.findLibrary("hilt-gradlePlugin").get())
    compileOnly(catalog.findLibrary("ksp-gradlePlugin").get())
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "countries.android.application"
            implementationClass = "dev.gustavo.countries.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "countries.android.library"
            implementationClass = "dev.gustavo.countries.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "countries.android.compose"
            implementationClass = "dev.gustavo.countries.buildlogic.AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "countries.android.hilt"
            implementationClass = "dev.gustavo.countries.buildlogic.AndroidHiltConventionPlugin"
        }
        register("jacoco") {
            id = "countries.jacoco"
            implementationClass = "dev.gustavo.countries.buildlogic.JacocoConventionPlugin"
        }
        register("jacocoAggregate") {
            id = "countries.jacoco.aggregate"
            implementationClass = "dev.gustavo.countries.buildlogic.JacocoAggregateConventionPlugin"
        }
    }
}
