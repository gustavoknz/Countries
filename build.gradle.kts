import org.gradle.api.tasks.testing.TestReport

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("countries.jacoco.aggregate")
    id("countries.ktlint")
}

// Ensure ktlint pre-commit hook is installed automatically
tasks.register("installKtlintHook") {
    group = "Verification"
    description = "Installs the ktlint pre-commit hook."
    dependsOn("addKtlintCheckGitPreCommitHook")
}

tasks.register<TestReport>("combinedAndroidTestReport") {
    group = "Reporting"
    description = "Combines Android instrumented test reports from all modules."
    destinationDirectory.set(layout.buildDirectory.dir("reports/androidTests/combined"))

    subprojects.forEach { subproject ->
        val resultsDir = subproject.layout.buildDirectory.dir("outputs/androidTest-results/connected/debug").get().asFile
        if (resultsDir.exists()) {
            testResults.from(resultsDir)
        }
    }
}
