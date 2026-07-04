# Countries App

A modern, high-performance, offline-first Android application that displays information about countries around the world. Built with Jetpack Compose, Clean Architecture, and the latest Android standards.

## 🚀 Technologies & Specs

- **Platform SDK**: [Android 15 (SDK 37)](https://developer.android.com/about/versions/15) - Fully targeting the latest platform features.
- **Language**: [Kotlin 2.4.0](https://kotlinlang.org/) with the new K2 compiler and [KSP2](https://kotlinlang.org/docs/ksp-overview.html).
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit with [Strong Skipping Mode](https://developer.android.com/develop/ui/compose/performance/skipping) optimized models.
- **Dependency Injection**: [Hilt 2.60](https://dagger.dev/hilt/) - Automated and scalable DI.
- **Build System**: [Gradle 9.6.1](https://gradle.org/) with **build-logic** (Convention Plugins) for scalable multi-module management.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) with type-safe routing via [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html).
- **Networking**: [Retrofit 3.0](https://square.github.io/retrofit/) & [OkHttp 5.4](https://square.github.io/okhttp/) with [Gson](https://github.com/google/gson).
- **Persistence**: [Room 2.8](https://developer.android.com/training/data-storage/room) - Robust local database with full offline support.
- **Paging**: [Paging 3.5](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) with `RemoteMediator` for seamless database-backed network loading.
- **Performance**: [Kotlinx Immutable Collections](https://github.com/Kotlin/kotlinx.collections.immutable) and `Sequences` for memory-efficient data processing.

## 🏗 Architecture

The app follows strict **Clean Architecture** principles and is modularized by layers and features:

### Modules
- `:app`: The entry point, manages navigation and global connectivity monitoring.
- `:build-logic`: Reusable Convention Plugins for Android, Compose, Hilt, and Jacoco.
- `:core:common`: Shared utilities, constants, and global error models.
- `:core:ui`: Common UI components, Theme, and stability-optimized UI models.
- `:core:testing`: Centralized testing helpers, fake data, and a shared Compose test runner.
- `:domain`: Pure Kotlin business logic, models, and Repository interfaces.
- `:data:local`: Room database implementation and DAOs.
- `:data:remote`: Retrofit API service and DTO models.
- `:data:repository`: Repository implementation coordinating Local and Remote data sources.
- `:feature:list`: The main country list and optimized search functionality.
- `:feature:detail`: Detailed view with shared element transitions.

### Build System Logic
The project uses a modern **Included Build** approach. All common configurations are extracted into `:build-logic`, allowing modules to simply apply IDs like `id("countries.android.library")` or `id("countries.android.compose")`. This ensures consistency and dramatically reduces boilerplate.

## 🛠 Testing Strategy

- **Unit Testing**: Focused on ViewModels, Use Cases, and Mappers using [JUnit 4](https://junit.org/), [MockK](https://mockk.io/), and [Truth](https://truth.dev/).
- **Flow Testing**: Using [Turbine](https://github.com/cashapp/turbine) to verify asynchronous data streams.
- **UI Testing**: Comprehensive instrumented tests using a shared `setCountriesContent` helper that manages Theme and Transition boilerplate. Verified for both Application and Library modules.
- **Code Coverage**: Aggregate [Jacoco](https://www.jacoco.org/jacoco/) reporting covering the entire project.

## ✨ Key Features
- **Offline First**: All data is cached in Room. Stale data is automatically cleared on refresh.
- **Performance Optimized**: Uses `Sequences` for large list mapping and `@Immutable` models to ensure Compose skippability.
- **Smart Search**: Debounced search that isolates results to maintain main list integrity.
- **Shared Transitions**: Fluid flag and name transitions when navigating from list to details.
- **Robust Error Handling**: Technical errors (403, Timeout, No Connection) are mapped to user-friendly messages and logged with detailed context in the repository layer.
