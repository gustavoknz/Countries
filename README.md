# Countries App

A modern, offline-first Android application that displays information about countries around the world. Built with Jetpack Compose, Clean Architecture, and MVI.

## 🚀 Technologies Used

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit.
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) - Standard way to incorporate Dagger dependency injection into an Android application.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) with [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) for type-safe routing.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) with [Gson](https://github.com/google/gson) for API communication.
- **Persistence**: [Room](https://developer.android.com/training/data-storage/room) - Robust local database with full offline support.
- **Paging**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) with `RemoteMediator` for seamless database-backed network loading and placeholders.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) - Kotlin-first image loading library.
- **Concurrency**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html).
- **Animations**: [Shared Element Transitions](https://developer.android.com/jetpack/compose/animation/shared-elements) for high-quality navigation polish.

## 🏗 Architecture

The app follows **Clean Architecture** principles and is modularized by layers and features:

### Modules
- `:app`: The entry point, manages navigation and global connectivity monitoring.
- `:core:common`: Shared utilities, constants, and global error models.
- `:core:ui`: Common UI components (Theme, Shimmer, Shared Test Tags).
- `:domain`: Business logic, pure Kotlin models, and Repository interfaces.
- `:data:local`: Room database implementation and DAOs.
- `:data:remote`: Retrofit API service and DTO models.
- `:data:repository`: Repository implementation coordinating Local and Remote data sources.
- `:feature:list`: The main country list and search functionality.
- `:feature:detail`: Detailed view for a specific country.

### UI Pattern: MVI-lite
The app uses a simplified Model-View-Intent (Action/State/Event) pattern:
- **Action**: UI sends actions to the ViewModel (e.g., `SearchQueryChanged`, `CountryClicked`).
- **State**: ViewModel exposes a single `StateFlow` (e.g., `DetailViewState`) for the UI to observe.
- **Event**: One-time side effects (e.g., navigation, showing a snackbar) are handled via a `SharedFlow`.

## 🛠 Testing Strategy

- **Unit Testing**: Focused on ViewModels, Use Cases, and Mappers using [JUnit 4/5](https://junit.org/), [MockK](https://mockk.io/), and [Truth](https://truth.dev/).
- **Flow Testing**: Using [Turbine](https://github.com/cashapp/turbine) to verify asynchronous data streams.
- **UI Testing**: State-based instrumentation tests using [Compose Testing Library](https://developer.android.com/jetpack/compose/testing) and [Espresso](https://developer.android.com/training/testing/espresso).

## ✨ Key Features
- **Offline First**: All data is cached in Room. Search results are isolated to maintain main list integrity.
- **Shimmer Loaders**: Custom shimmering skeleton loaders for better perceived performance.
- **Paging Placeholders**: Smooth scrolling through the country list with ghost cards.
- **Shared Transitions**: Fluid flag and name transitions when navigating to details.
- **Localized Error Handling**: Technical errors (403, Timeout, No Connection) are mapped to user-friendly messages.
