# QREZZY

QR scanner and generator Android app built with Kotlin and Jetpack Compose.

## Tech Stack

- Kotlin
- Jetpack Compose
- Navigation Compose
- CameraX
- Room
- Hilt
- Coroutines & Flow
- Clean Architecture
- Material 3

## Requirements

- Android Studio (latest stable version)
- JDK 17
- minSdk 28
- targetSdk 36

The project uses minSdk 28 to keep compatibility with most active Android devices while avoiding
legacy platform limitations.

## Code Quality

The project uses automated code quality tools:

- Ktlint — code formatting and style checks
- Detekt — static code analysis
- EditorConfig — shared IDE formatting configuration

### Run checks

```bash
./gradlew ktlintCheck detekt
./gradlew ktlintFormat