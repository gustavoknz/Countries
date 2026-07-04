plugins {
    `kotlin-dsl`
}

group = "dev.gustavo.countries.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.7.3")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    compileOnly("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
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
