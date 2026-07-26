# Countries App

A modern, high-performance, offline-first Android application that displays information about countries around the world. Built with Jetpack Compose, Clean Architecture, and the latest Android standards.

## 🚀 Technologies & Specs

- **Platform SDK**: [Android 15 (SDK 37)](https://developer.android.com/about/versions/15) - Fully targeting the latest platform features.
- **Language**: [Kotlin 2.4.10](https://kotlinlang.org/) with the new K2 compiler and [KSP2](https://kotlinlang.org/docs/ksp-overview.html).
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit with **Adaptive Two-Pane** support.
- **Adaptive Layout**: Uses [Material 3 Adaptive](https://developer.android.com/jetpack/compose/layouts/adaptive) for seamless Phone $\to$ Tablet transitions.
- **Dependency Injection**: [Hilt 2.60.1](https://dagger.dev/hilt/) - Fully migrated to KSP.
- **Build System**: [Gradle 9.6.1](https://gradle.org/) with **build-logic** (Convention Plugins).
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) with type-safe routing.
- **Persistence**: [Room 2.8.4](https://developer.android.com/training/data-storage/room) - Normalized local database.
- **Paging**: [Paging 3.5](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) with `RemoteMediator`.

## 🏁 Getting Started

1. **Install KtLint Hook**:
   ```bash
   ./gradlew installKtlintHook
   ```
2. **Generate Module Graph**:
   ```bash
   ./gradlew generateModuleGraph
   ```
3. **Build the Project**:
   ```bash
   ./gradlew assembleDebug
   ```

## 🏗 Architecture

The app follows strict **Clean Architecture** principles:

### Modules
- `:app`: Entry point, adaptive orchestration, and global state.
- `:build-logic`: Reusable Convention Plugins.
- `:core:common`: Shared utilities, global error models (`DataError`), and navigation routes.
- `:core:ui`: Token-based Design System, reusable components, and **Multipreview** annotations.
- `:core:testing`: Centralized test fakes and the **Robot Pattern** base.
- `:domain`: Pure Kotlin business logic and repository interfaces.
- `:data:local`: Room database with automated cache pruning.
- `:data:remote`: Retrofit-based network layer.
- `:data:repository`: Domain-aligned data coordination.

### Adaptive Two-Pane Layout
The app implements a professional **List-Detail** adaptive scaffold.
- **Compact (Phones)**: Single-pane navigation flow.
- **Expanded (Tablets/Foldables)**: Dual-pane layout where the list and detail are visible simultaneously.
- **Adaptive Grid**: The country list automatically adjusts its column count based on available width, ensuring optimal density on any device.

## 🛠 Testing Strategy

- **Robot Pattern**: Highly readable and maintainable UI verification scripts.
- **Multipreview Strategy**: Centralized `@CombinedPreviews` annotation to verify UI on multiple devices and font scales (0.85x to 2.0x) instantly.
- **Screenshot Testing**: **Roborazzi** for JVM-based visual regression.
    - Run verification: `./gradlew verifyRoborazziDebug`
- **Unit Testing**: 100% coverage on critical logic (Mappers, ViewModels, Repositories).
- **Code Coverage**: Aggregate **JaCoCo** reporting refined to track meaningful business logic while excluding UI boilerplate.
    - Generate report: `./gradlew jacocoFullReport`

## ✨ Key Features

- **Offline-First & Normalized**: Prevents data duplication and ensures availability without a network.
- **Intelligent Cache Pruning**: The local database automatically removes orphaned countries not in current search results.
- **Accessibility Optimized**: Full support for screen readers via semantic grouping and accessibility labels.
- **Fluid Shared Transitions**: Premium animations that maintain context when moving between country list and details.
- **Senior-Grade Quality Gates**: Strict KtLint and Detekt enforcement with custom architectural rules.
