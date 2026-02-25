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

## Testing
- Unit tests live in each module’s `src/test/kotlin` folder.
- Integration tests use **Turbine** for Flow verification.
- JaCoCo coverage is aggregated across all modules via the `jacocoMergedReport` task.

## License
This project is for interview purposes only and is not intended for production distribution. Feel free to explore, modify, and learn from the code.
