# Countries App

A modern, high-performance, offline-first Android application that displays information about countries around the world. Built with Jetpack Compose, Clean Architecture, and the latest Android standards.

## 🚀 Technologies & Specs

- **Platform SDK**: [Android 15 (SDK 37)](https://developer.android.com/about/versions/15) - Fully targeting the latest platform features.
- **Language**: [Kotlin 2.4.10](https://kotlinlang.org/) with the new K2 compiler and [KSP2](https://kotlinlang.org/docs/ksp-overview.html).
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit with [Strong Skipping Mode](https://developer.android.com/develop/ui/compose/performance/skipping) optimized models.
- **Dependency Injection**: [Hilt 2.60.1](https://dagger.dev/hilt/) - Fully migrated to KSP.
- **Build System**: [Gradle 9.6.1](https://gradle.org/) with **build-logic** (Convention Plugins) for scalable multi-module management.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) with type-safe routing and lifecycle-guarded transitions.
- **Networking**: [Retrofit 3.0](https://square.github.io/retrofit/) & [OkHttp 5.4](https://square.github.io/okhttp/) with [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html).
- **Persistence**: [Room 2.8.4](https://developer.android.com/training/data-storage/room) - Normalized local database with full offline support.
- **Paging**: [Paging 3.5](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) with `RemoteMediator` and intelligent cache freshness logic.
- **Performance**: [Kotlinx Immutable Collections](https://github.com/Kotlin/kotlinx.collections.immutable) and `Sequences` for memory-efficient data processing.

## 🏗 Architecture

The app follows strict **Clean Architecture** principles and is modularized by layers and features:

### Modules
- `:app`: The entry point, manages navigation and global connectivity monitoring.
- `:build-logic`: Reusable Convention Plugins for Android, Compose, Hilt, JaCoCo, KtLint, and Detekt.
- `:core:common`: Shared utilities, constants, global error models, and the `Region` enum.
- `:core:ui`: Common UI components, Theme, and stability-optimized UI models.
- `:core:testing`: Centralized testing helpers, fake data, and the **Robot Pattern** base.
- `:core:detekt-rules`: Custom static analysis rules for architectural enforcement.
- `:domain`: Pure Kotlin business logic, models, and Repository interfaces.
- `:data:local`: Room database implementation, DAOs, and normalized entities.
- `:data:remote`: Retrofit API service and DTO models.
- `:data:repository`: Repository implementation coordinating Local and Remote data sources.
- `:feature:list`: The main country list and optimized search functionality.
- `:feature:detail`: Detailed view with sectioned UI and fluid transitions.

### Build System Logic
The project uses a modern **Included Build** approach. All common configurations are extracted into `:build-logic`, allowing modules to simply apply IDs like `id("countries.android.library")` or `id("countries.android.hilt")`. 

### Automated Module Graph
A custom Gradle plugin (`countries.project.graph`) provides the `generateModuleGraph` task, which generates a [Mermaid](https://mermaid.js.org/) dependency graph in [MODULE_GRAPH.md](./MODULE_GRAPH.md). This graph is automatically verified by the CI pipeline to ensure documentation is always in sync with the actual module structure.

### Screenshot Testing (Roborazzi)
The project uses **Roborazzi** to perform screenshot testing on the JVM.
- To record new baseline screenshots: `./gradlew recordRoborazziDebug`
- To verify against baseline: `./gradlew verifyRoborazziDebug`
- Screenshots are saved in the `screenshots` folder within each module.

## 🛠 Testing Strategy

- **Robot Pattern**: Implemented for all UI and Flow tests, leading to highly readable and maintainable verification scripts.
- **Unit Testing**: Focused on ViewModels, Use Cases, and Mappers using [JUnit 4](https://junit.org/), [MockK](https://mockk.io/), and [Truth](https://truth.dev/).
- **Flow Testing**: Using [Turbine](https://github.com/cashapp/turbine) to verify asynchronous data streams.
- **UI Testing**: Comprehensive instrumented tests with shared element transition support.
- **Screenshot Testing**: Integrated **Roborazzi** for JVM-based visual regression testing. This allows catching UI regressions without an emulator.
- **Code Coverage**: Aggregate [JaCoCo](https://www.jacoco.org/jacoco/) reporting covering the entire project logic.

## 👮 Quality Gates (Senior Level Enforcement)

- **KtLint**: Enforces the official Kotlin Style Guide. Automatically installed as a Git **pre-commit hook**.
- **Detekt**: Performs deep static analysis. Configured with modern thresholds for Compose development.
- **Custom Lint Rules**: Includes a custom rule that prevents ViewModels from depending on implementation classes directly, enforcing the **Dependency Inversion Principle**.
- **CI Enforcement**: The CI pipeline validates unit tests, code coverage, static analysis (Ktlint/Detekt), and module graph integrity.

## ✨ Key Features

- **Offline First & Normalized**: All data is cached in a normalized Room database. Countries are stored uniquely, preventing data duplication.
- **Intelligent Cache**: `RemoteMediator` skips network refreshes if local data is fresh (less than 1 hour old).
- **Safety First Navigation**: Lifecycle-guarded navigation prevents common "rapid-click" crashes.
- **Standardized R8/ProGuard**: Obfuscation rules are managed at the module level via `consumer-rules.pro`, ensuring high-quality shrinking and optimization.
- **Performance Optimized**: Uses `Sequences` for mapping and `@Immutable` models for Compose stability.
- **Fluid UI**: Shared element transitions and sectioned animations provide a premium user experience.
- **Type-Safe Domain**: Enforced use of enums (like `Region`) instead of raw strings.
