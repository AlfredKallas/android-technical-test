# Leboncoin Interview App – README

## Overview
This is the interview project for **Leboncoin**. The original codebase contained numerous bugs and architectural issues. The overhaul fixes those problems and delivers a clean, modular, and production‑ready Android app that displays a list of albums fetched from a backend service.

## What Was Fixed

| Area | Issue | Solution |
|------|-------|----------|
| **Coroutines** | GlobalScope was used for network calls, causing leaks and uncontrolled lifecycles. | Replaced GlobalScope with structured concurrency (ViewModelScope / repository‑scoped flows). |
| **Performance & Lag** | No caching, all data fetched on‑the‑fly; images re‑downloaded each time. | • Added a **Room** database cache.<br>• Loaded data via **Paging3** (`PagingData`).<br>• Enabled **disk caching** for images and created a custom **ImageLoader** with an interceptor that injects required HTTP headers. |
| **Architecture** | Ad‑hoc dependencies, no separation of concerns, violated SOLID. | • Integrated **Dagger Hilt** for dependency injection.<br>• Adopted a clear **MVVM** stack (Data → Repository → ViewModel → UI). |
| **Modularisation** | Single monolithic module. | Split the project into logical modules:
| | **App** | Entry point, application class, DI setup. |
| | **Core** | Shared utilities: 
- `core:common` – generic helpers, Hilt core bindings.
- `core:database` – Room entities & DAOs.
- `core:data` – Repositories & data‑layer logic.
- `core:network` – Retrofit API, OkHttp client, interceptors.
- `core:analytics` – Analytics scaffolding. |
| | **Feature** | One module per feature: 
- `feature:albumslist` – Album list screen, ViewModel, UI state/models.
- `feature:albumDetails` – Album detail screen, ViewModel, UI state/models.
- `feature:favourites` – Favourite handling, DB caching. |
| | **Resources** | Strings, drawables, themes, and other assets. |
| **Build Types** | Only a debug configuration existed. | Added **debug** and **release** build types. Release builds enable minification (R8) while library modules keep minification disabled to avoid class stripping. |
| **Localization** | Hard‑coded UI text. | All user‑visible strings moved to `strings.xml`. The app automatically updates UI when the device language changes. |
| **Navigation** | Manual fragment transactions. | Switched to **Navigation‑Compose (Nav3)** with a single NavHost. |
| **Adaptive UI** | UI only designed for phones. | Implemented **adaptive UI** using Navigation‑Compose’s `adaptive` support – list‑detail layout works on tablets and larger screens. |

## Architecture: Multi-Module MVVM

The application follows a **Multi-Module MVVM (Model-View-ViewModel)** architecture pattern, organized into three primary layers: **Data → Repository → Presentation**.

### Why Multi-Module MVVM for a Small App?
While the app is currently small, this architecture was chosen for several critical reasons:
- **Scalability**: It establishes a robust foundation that allows for seamless expansion as new features are added, without cluttering existing code.
- **Separation of Concerns**: Each module has a single responsibility, making the codebase easier to navigate, test, and maintain.
- **Improved Build Times**: By splitting the app into smaller modules, Gradle can perform parallel builds and avoid re-compiling unrelated parts of the app during development.
- **Testability**: Decoupling the data layer from the UI allows for isolated unit testing of business logic and repository flows.

### Unidirectional Data Flow (UDF)
This architecture ensures a strict **Unidirectional Data Flow (UDF)**:
1.  **State** flows down from the ViewModel to the UI.
2.  **Events** flow up from the UI to the ViewModel.
3.  **Data** flows from the Data Source (Network/Database) through the Repository to the ViewModel.

By centralizing the UI state within the ViewModel and ensuring data only flows in one direction, we minimize side effects, make the app behavior predictable, and simplify debugging.

## Convention Plugins & Extensibility

This project leverages the **Convention Plugins** pattern (using the `build-logic` directory) to centralize and share Gradle configurations across modules.

### Flavor Plugins
- **`art.android.application.flavors`**: Standardizes product flavors and build configurations for the application module.
- **`art.android.library.flavors`**: Ensures consistent flavor configurations across all library modules.

### Build Variants

The project includes the following build variants:

-   `dev`: For development purposes. It has a `.dev` application ID suffix.
-   `prod`: The production-ready variant.


### Extensibility Pattern
The architecture is designed to be highly extensible. By creating new plugins in the `build-logic` module, you can easily share recurrent configurations and dependencies. For example:
- **Dependency Bundles**: Group related dependencies (e.g., `Compose`, `Retrofit`, `Hilt`) into a single plugin that can be applied to any module.
- **Shared Build Logic**: Centralize logic for versioning, code quality tools (like JaCoCo), and ProGuard rules.
- **Uniform Module Setup**: Create a "base" plugin that applies common plugins (KSP, Hilt, Kotlin) and sets up standard Android configurations (compileSdk, minSdk, JVM target) to reduce boilerplate in `build.gradle.kts` files.

This approach ensures a cleaner, more maintainable build system and adheres to the "Don't Repeat Yourself" (DRY) principle at the build level.

## Project Structure
```
android-technical-test/
├─ app/                     # Application module (entry point)
├─ core/
│   ├─ common/              # Shared utilities, Hilt core bindings
│   ├─ database/            # Room DB, entities, DAOs
│   ├─ data/                # Repositories, use‑cases
│   ├─ network/             # Retrofit API, OkHttp client, interceptors
│   └─ analytics/           # Analytics scaffolding
├─ feature/
│   ├─ albumslist/          # Album list UI + ViewModel
│   ├─ albumDetails/        # Album detail UI + ViewModel
│   └─ favourites/          # Favourites handling & UI
├─ resources/               # Strings, drawables, themes
└─ build.gradle.kts         # Root Gradle script
```

## Key Dependencies
- **Kotlin 1.9**, **Coroutines**, **Flow**
- **Retrofit 2**, **OkHttp 4**, **Coil** (image loading)
- **Room 2**, **Paging 3**
- **Dagger Hilt** (DI)
- **Navigation‑Compose (Nav3)** + **Adaptive UI**
- **Jetpack Compose** UI toolkit

## Building & Running
```bash
# Debug build (no minification)
./gradlew assembleDebug

# Release build (minified, ProGuard/R8 applied)
./gradlew assembleRelease
```
The app will automatically pick the device language and display the appropriate localized strings.

## Testing Strategy
The project employs a multi-layered testing strategy to ensure reliability and performance:
- **Unit Tests**: Standard JUnit & Mockito tests for ViewModels, Repositories, and Mappers.
- **UI Tests (Robolectric)**: Automated Compose UI tests running on the JVM using Robolectric. These tests validate:
    - **UI States**: Loading, Error, Success, and Empty states for all feature screens.
    - **Interaction**: Basic user interactions like clicking retry buttons.
    - **Composition**: Verification of UI component placement and content using centralized `TestTags`.
- **Flow Verification**: Uses **Turbine** for isolated testing of asynchronous Flow emissions.
- **Code Coverage**: Integrated **JaCoCo** to track and report coverage across all modules.

### Running Tests
To run the full suite of unit and UI tests for a specific feature, use the following Gradle commands:
```bash
# Albums List feature tests
./gradlew :feature:albumslist:testDevDebugUnitTest

# Favourites feature tests
./gradlew :feature:favourites:testDevDebugUnitTest

# Album Details feature tests
./gradlew :feature:albumDetails:testDevDebugUnitTest
```

## Code Coverage

The project is configured to generate code coverage reports for each module using JaCoCo. 

To generate a coverage report for a specific module, run the `create<Variant>CombinedCoverageReport` task. For example, to generate a report for the `devDebug` variant of all modules, run:

```bash
./gradlew createDevDebugCombinedCoverageReport
```

The HTML report will be generated in each module's build directory: `feature/albumslist/build/reports/jacoco/createDevDebugCombinedCoverageReport/html/index.html`.

I would have liked to be able to aggregate the resuklts into one but was not able to do it.

> [!TIP]
> UI tests are executed on the `devDebug` variant by default. Due to the project's use of API 36, Robolectric tests are configured to emulate **Android SDK 35** via `@Config(sdk = [35])` to ensure environment stability.

## License
This project is for interview purposes only and is not intended for production distribution. Feel free to explore, modify, and learn from the code.
