# QREZZY — Agent Instructions

This document defines repository-wide instructions for AI coding agents working on QREZZY.

These instructions are vendor-neutral and should be followed by any coding agent working in this
repository.

---

## 1. Project

QREZZY is a native Android application for scanning, generating, managing, and storing QR codes.

Package:

`software.mazur.qrezzy`

The project is currently a single Gradle module:

`:app`

### Technology stack

The project uses:

- Kotlin
- Jetpack Compose
- Material 3
- Kotlin Coroutines
- Flow / StateFlow
- Room
- Navigation Compose
- Hilt
- CameraX
- ML Kit Barcode Scanning
- ZXing
- Firebase Crashlytics
- JUnit 4
- MockK
- Turbine
- kotlinx-coroutines-test
- Detekt
- ktlint
- Gradle Kotlin DSL
- Gradle Version Catalog

Do not assume that a library, architecture, or pattern is used only because it is common in Android
development.

Inspect the repository before making implementation decisions.

---

## 2. Authoritative Principle

**Existing project conventions are the primary source of truth.**

Before introducing a new:

- pattern,
- abstraction,
- package,
- architectural layer,
- class type,
- naming convention,
- utility,
- reusable component,
- dependency,
- error-handling mechanism,

first search the repository for an analogous implementation.

Follow this process:

1. Inspect relevant existing code.
2. Search for analogous implementations.
3. Understand the established project pattern.
4. Reuse that pattern when appropriate.
5. Introduce a new pattern only when the existing approach cannot reasonably solve the problem.

Do not infer a repository-wide convention from a single file.

When these instructions conflict with an established project pattern, prefer the existing project
convention unless:

- the task explicitly requires changing it,
- the existing implementation is clearly incorrect,
- it introduces a security or data-loss risk,
- or the task is an intentional architectural refactor.

Prefer repository evidence over generic Android conventions.

---

## 3. Project Structure

The production code is organized under:

```text
app/src/main/java/software/mazur/qrezzy/
├── core/
├── data/
├── domain/
├── feature/
└── navigation/
```

### `core/`

Cross-feature and framework-facing shared code.

Examples include:

- `core/designsystem/`
- `core/qr/`
- `core/common/`
- `core/localization/`

Reusable Compose UI belongs under:

```text
core/designsystem/components/
```

Framework-facing QR rendering and decoding utilities belong under:

```text
core/qr/
```

### `domain/`

Business/domain-level code.

Existing structure follows:

```text
domain/<area>/
├── model/
├── repository/
├── usecase/
└── mapper/
```

Existing domain areas include:

- `qr`
- `settings`

### `data/`

Persistence and concrete repository implementations.

Existing structure follows:

```text
data/<area>/
├── local/
├── repository/
├── mapper/
└── di/
```

### `feature/`

Feature-specific UI and presentation code.

Typical feature content includes:

```text
feature/<name>/
├── <Feature>Screen.kt
├── <Feature>ViewModel.kt
├── model/
├── mapper/
├── components/
└── navigation/
```

Not every feature needs every directory.

Before creating a new package, inspect an analogous existing feature and follow its structure.

Do not invent a new repository-wide directory structure without a clear reason.

---

## 4. Architecture

Follow the existing Clean Architecture-inspired separation.

General principles:

- feature-based presentation organization,
- separation between UI, domain, and data responsibilities,
- unidirectional data flow,
- business logic outside composables,
- repository abstractions between presentation/domain and persistence,
- domain logic expressed through use cases where consistent with existing code.

Prefer simple solutions over unnecessary architectural abstractions.

Do not introduce architecture solely for theoretical purity.

Do not create duplicate abstractions, helpers, utilities, extensions, or components without first
searching for an existing equivalent.

---

## 5. Naming Conventions

Follow existing QREZZY naming.

### ViewModels

Use:

```text
<Feature>ViewModel
```

Example:

```text
ScannerViewModel
GeneratorViewModel
SettingsViewModel
```

### UI state

Use:

```text
<Feature>UiState
```

UI state is normally represented by a `data class` with sensible defaults.

### UI events

Use:

```text
<Feature>UiEvent
```

Use a `sealed interface`.

Prefer:

- `data object` for events without payload,
- `data class` for events with payload.

Do not copy the existing plain `object` inconsistency in `HistoryDetailsUiEvent`.

### Use cases

Use:

```text
<Verb><Noun>UseCase
```

Use cases normally expose:

```kotlin
operator fun invoke(...)
```

Keep one focused operation per use case.

### Repositories

Domain interface:

```text
<Noun>Repository
```

Data implementation:

```text
<Noun>RepositoryImpl
```

### Room entities

Use:

```text
<Noun>Entity
```

### Mappers

QREZZY uses top-level extension functions rather than mapper classes.

Prefer:

```kotlin
fun QrEntity.toDomain(): Qr
fun Qr.toEntity(): QrEntity
```

Do not introduce mapper classes/interfaces when an extension-function mapper matches the existing
pattern.

### DTOs

QREZZY currently has no remote-data layer and no established DTO convention.

Do not introduce a DTO architecture without a task that actually requires a remote/external data
boundary.

---

## 6. Jetpack Compose

Prefer:

- stateless composables where practical,
- state hoisting,
- immutable UI state,
- lifecycle-aware state collection,
- small and focused composables,
- reusable QREZZY design-system components,
- clear separation between UI rendering and application logic.

Keep business logic out of composables.

Do not duplicate state unnecessarily.

Use `remember`, derived state, and side-effect APIs only when their lifecycle and purpose are
understood.

Before creating a reusable component, search:

```text
core/designsystem/components/
```

Existing reusable components follow the:

```text
Qrezzy*
```

naming convention.

Examples include:

```text
QrezzyButton
QrezzySwitch
QrezzyTopBar
QrezzyPopup
```

Do not duplicate an existing design-system component.

---

## 7. ViewModels and State

For new ViewModels, prefer:

```kotlin
StateFlow<UiState>
```

and lifecycle-aware UI collection:

```kotlin
collectAsStateWithLifecycle()
```

Persistent UI state should not use one-off event streams.

One-off actions such as:

- navigation,
- toast messages,
- sharing,
- downloads,
- back navigation,

should follow the existing `UiEvent` + `SharedFlow` pattern when appropriate.

Existing screens normally collect one-off events using:

```kotlin
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        ...
    }
}
```

`GeneratorViewModel` currently uses Compose `mutableStateOf`.

Treat this as an existing exception, not the preferred pattern for new ViewModels.

Do not refactor it solely for consistency unless explicitly requested.

Do not expose mutable state objects directly to UI code.

---

## 8. Coroutines and Flow

Use structured concurrency.

Never use:

```kotlin
GlobalScope
```

Respect coroutine cancellation.

Do not accidentally swallow coroutine cancellation when catching broad exceptions.

Avoid blocking operations on inappropriate dispatchers.

Follow existing project conventions for:

- dispatcher injection,
- Flow transformations,
- `stateIn`,
- sharing strategies,
- coroutine testing,
- lifecycle-aware collection.

Do not introduce custom coroutine scopes without a clear lifecycle reason.

---

## 9. Dependency Injection

QREZZY uses Hilt.

Existing modules are installed in:

```kotlin
SingletonComponent::class
```

and existing application-level dependencies are generally scoped with:

```kotlin
@Singleton
```

### `@Binds`

Use `@Binds` for interface-to-implementation bindings.

Typical example:

```text
Repository → RepositoryImpl
```

Existing binding modules use abstract Hilt modules.

### `@Provides`

Use `@Provides` when explicit construction logic is required and constructor injection or `@Binds`
is not sufficient.

The Room database setup follows this pattern.

Prefer constructor injection whenever possible.

Do not introduce:

- custom qualifiers,
- custom scopes,
- different Hilt components,
- unnecessary DI modules,

without a clear requirement.

Add bindings to an appropriate existing module when possible.

---

## 10. Error Handling

QREZZY does not currently use:

- custom `Result<T>` wrappers,
- `Either`,
- sealed domain error hierarchies,
- custom project-wide exception types.

Do not introduce these abstractions unless the task explicitly requires redesigning error handling.

Repository and DAO failures generally propagate to the caller.

Failure-prone operations in ViewModels commonly use:

```kotlin
runCatching {
    ...
}
    .onSuccess {
        ...
    }
    .onFailure { error ->
        ...
    }
```

Failures may result in:

- an appropriate `UiEvent`,
- user-visible feedback,
- exception recording through `CrashReporter`.

Use:

```text
CrashReporter
```

for existing logging/crash-reporting workflows when consistent with analogous code.

Do not silently swallow exceptions.

Do not expose infrastructure-specific exceptions directly to UI unless the surrounding code
explicitly expects that behavior.

User-facing errors must use localized resources where possible.

Do not introduce new raw English user-facing error strings inside ViewModels.

---

## 11. QR Domain

QR scanning and generation are core application functionality.

Treat scanned QR data as untrusted external input.

### Live camera scanning

Current implementation uses:

```text
CameraX + ML Kit Barcode Scanning
```

Live QR analysis is handled through the existing scanner analyzer infrastructure.

Do not replace this stack during unrelated work.

### Static image decoding

QR decoding from selected image files currently uses:

```text
ZXing
```

### QR generation

QR generation currently uses:

```text
ZXing
```

with custom bitmap rendering.

Preserve the current split:

```text
Live camera scan → ML Kit
Static image decode → ZXing
QR generation → ZXing
```

unless the task explicitly changes the implementation.

### Supported QR types

Existing domain types include:

- TEXT
- URL
- PHONE
- EMAIL
- WIFI
- CONTACT
- SMS
- GEO_LOCATION

Preserve supported behavior unless explicitly changing it.

### Security

Never automatically execute sensitive actions based solely on scanned QR content.

Do not automatically:

- open external URLs,
- connect to Wi-Fi networks,
- initiate calls,
- initiate messages,
- launch sensitive external intents.

Such actions must require explicit user interaction.

Validate and parse QR content defensively.

Do not assume scanned content is trustworthy or well-formed.

---

## 12. Camera and Permissions

Follow the existing CameraX lifecycle and permission implementation.

Camera runtime permissions are currently handled using standard Android APIs and:

```text
ActivityResultContracts.RequestPermission
```

Do not introduce an additional permission library without a clear need.

Camera access must:

- respect Android runtime permissions,
- handle denied permissions gracefully,
- handle permanently denied permissions where applicable,
- respect lifecycle boundaries,
- release/unbind camera resources appropriately.

Do not request unnecessary permissions.

Before using a platform API, verify the project's current `minSdk`.

Do not rely on APIs unavailable on the minimum supported Android version without compatibility
handling.

---

## 13. Room and Persistence

QREZZY uses one Room database with explicit incremental migrations.

Existing migration naming follows:

```text
MIGRATION_1_2
MIGRATION_2_3
MIGRATION_3_4
```

Schema changes must preserve user data whenever reasonably possible.

When changing:

- entities,
- columns,
- tables,
- indexes,
- relationships,
- converters,
- persisted schema,

inspect the existing migration implementation first.

Every persisted schema change should include an appropriate explicit migration.

Do not enable:

```kotlin
fallbackToDestructiveMigration(...)
```

as a shortcut.

Do not introduce equivalent destructive behavior without explicit approval.

Do not change the Room database version unless the persisted schema actually changes and the
migration strategy has been considered.

Follow existing DAO conventions:

- `suspend` functions for one-shot operations,
- `Flow` for observable data.

Do not expose Room entities directly to UI code.

Use existing mapping extensions between persistence and domain models.

If migration testing infrastructure exists or is introduced intentionally, update migration tests
together with schema changes.

---

## 14. Localization

QREZZY supports multiple locales.

All new user-facing text must use Android string resources.

Use:

```text
res/values/strings.xml
```

and corresponding localized resources.

Do not introduce new hardcoded user-facing strings in:

- ViewModels,
- composables,
- UI events,
- user-visible error messages.

Preserve existing translations when changing strings.

If a new string is added, update supported translations where feasible and consistent with the task.

Do not move internal identifiers, log messages, technical constants, or debugging strings into
resources unnecessarily.

---

## 15. Accessibility

Do not reduce existing accessibility support.

Meaningful icon-only actions should have appropriate accessibility descriptions.

Decorative images/icons should generally use:

```kotlin
contentDescription = null
```

when they convey no additional semantic information.

For new interactive UI:

- provide understandable labels,
- preserve usable touch targets,
- provide appropriate semantics,
- consider TalkBack behavior,
- avoid redundant accessibility descriptions.

Do not assume accessibility coverage is complete simply because existing components contain some
`contentDescription` values.

---

## 16. Dependencies

All dependency and plugin versions are managed through:

```text
gradle/libs.versions.toml
```

Do not hardcode:

- dependency versions,
- Maven coordinates,
- plugin versions

inside module build scripts.

When adding a dependency:

1. Check whether existing project functionality already solves the problem.
2. Check whether an existing dependency can be reused.
3. Add the version/library alias to the Version Catalog.
4. Reference the dependency through the generated `libs.*` accessor.

Do not update unrelated dependencies.

Do not perform broad version upgrades as part of unrelated work.

Do not add a third-party library for functionality that can reasonably be solved using existing
project dependencies or Android APIs.

---

## 17. Formatting and Static Analysis

The project uses both:

```text
Detekt
ktlint
```

Detekt configuration lives under:

```text
config/detekt/
```

ktlint is configured through Gradle and has an existing baseline.

Follow the configured rules rather than generic defaults.

Avoid unrelated formatting changes.

Do not reformat entire files when changing only a small section unless required by the configured
formatter.

Do not remove or broadly regenerate lint baselines without explicit reason.

Fix new static-analysis or formatting violations caused by the current change.

Current CI runs Detekt but does not currently enforce `ktlintCheck`.

Do not claim CI validates ktlint unless the CI workflow actually does so.

---

## 18. Testing

The unit-test stack uses:

- JUnit 4
- kotlinx-coroutines-test
- Turbine
- MockK

### Naming

Test class:

```text
<ClassName>Test
```

Test methods use behavior-oriented backtick names, typically:

```kotlin
@Test
fun `should return qr when input is valid`() {
    ...
}
```

### Coroutine tests

Use the existing:

```text
MainDispatcherRule
```

for ViewModel tests where appropriate.

Use:

```kotlin
runTest
```

for coroutine-based tests.

### Flow testing

Use Turbine when verifying a sequence of Flow emissions.

For simple state checks, reading:

```kotlin
viewModel.uiState.value
```

may be sufficient when consistent with analogous tests.

### Fakes vs mocks

For project-owned abstractions, prefer existing hand-written fakes.

Examples:

```text
FakeQrRepository
FakeAppSettingsRepository
FakeCrashReporter
FakeTimeProvider
```

Use MockK primarily for concrete framework-adjacent or external-facing classes when introducing a
fake would not provide meaningful value.

Existing examples include:

```text
QrImageDecoder
QrBitmapGenerator
QrezzyLocaleManager
```

Do not mock project-owned interfaces automatically when an established fake exists.

### Test scope

Prioritize behavioral tests over implementation-detail tests.

Important changes to:

- ViewModels,
- use cases,
- repositories,
- mappers,
- QR parsing,
- persistence,
- state transitions,

should normally include or update relevant tests.

Test:

- expected behavior,
- edge cases,
- failure paths,
- state transitions,
- regressions relevant to the change.

Do not create meaningless tests solely to increase coverage.

Do not rewrite unrelated tests.

### Instrumented and Compose UI tests

The project currently has dependencies for instrumented/UI testing, but no established real
UI-testing convention.

Do not introduce a new Compose UI/instrumented testing architecture as part of an unrelated task
unless explicitly requested.

Preserve the existing unit-test-oriented scope by default.

---

## 19. Navigation

Navigation currently uses sealed destination/route types with string routes.

Feature sub-flows may use their own nested:

```text
NavHost
NavController
```

Follow existing navigation structure before introducing new destinations.

Do not flatten or redesign navigation hierarchy during unrelated feature work.

Before adding a new route:

1. Inspect the nearest existing feature flow.
2. Follow the existing destination naming and route pattern.
3. Keep feature-specific navigation inside the relevant feature when appropriate.

---

## 20. Before Modifying Code

For every non-trivial task:

1. Understand the requested behavior.
2. Inspect the relevant existing implementation.
3. Search for analogous code elsewhere in the repository.
4. Identify affected files and packages.
5. Identify architecture and behavioral constraints.
6. Identify relevant tests.
7. Establish a focused implementation plan.
8. Only then begin editing.

Do not start rewriting code solely from the task description.

Ask for clarification when a genuine requirement ambiguity cannot be resolved from:

- existing code,
- tests,
- configuration,
- documentation.

---

## 21. Scope Control

Keep changes focused on the requested task.

Do not:

- refactor unrelated code,
- rename unrelated files,
- reorganize packages unnecessarily,
- redesign architecture opportunistically,
- change unrelated public APIs,
- update unrelated dependencies,
- perform speculative cleanup,
- remove code only because it appears unused,
- introduce new libraries without need,
- replace existing infrastructure during unrelated work.

Small adjacent fixes are acceptable only when directly required for the requested change.

Do not modify known legacy inconsistencies merely for stylistic consistency unless requested.

---

## 22. Validation

Use the repository's CI workflow as the authoritative baseline for required validation.

Current CI runs:

```bash
./gradlew detekt
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

During development, run the smallest relevant test scope first when useful.

Before completing a normal code change, run relevant validation.

Typical full validation:

```bash
./gradlew detekt
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

Run:

```bash
./gradlew ktlintCheck
```

when formatting-related changes are relevant or when explicitly requested.

Do not claim `ktlintCheck` is a CI requirement unless CI is changed to enforce it.

Before completion:

- fix failures caused by the current change,
- do not hide failing tests,
- do not disable failing tests merely to obtain a green build,
- distinguish pre-existing failures from failures caused by the current change.

Never claim that a command passed unless it was actually executed successfully.

If validation cannot be executed, state that clearly.

---

## 23. Git

Do not create commits unless explicitly requested.

Do not push changes unless explicitly requested.

Do not:

- force push,
- rewrite Git history,
- delete branches,
- discard unrelated working-tree changes,
- reset user changes,
- run destructive Git cleanup commands

without explicit approval.

Before completion, review the final diff.

When a commit is explicitly requested, keep it focused on the requested change.

---

## 24. Safety and Destructive Operations

Do not perform destructive operations without explicit approval.

This includes operations that may:

- delete user data,
- reset databases,
- remove files,
- rewrite Git history,
- overwrite important configuration,
- remove application functionality,
- invalidate persisted data,
- expose secrets.

Never commit:

- API keys,
- passwords,
- tokens,
- signing credentials,
- private keys,
- local machine configuration,
- secrets from CI or Firebase configuration.

Respect `.gitignore` and existing secret-management mechanisms.

Treat Room schema changes and persisted user data as safety-sensitive operations.

---

## 25. Definition of Done

A task is not complete merely because code has been generated.

Before declaring a task complete:

1. Review all changed files.
2. Confirm the changes match the requested scope.
3. Check for unintended modifications.
4. Run relevant tests.
5. Run Detekt when appropriate.
6. Run the affected build when appropriate.
7. Run other relevant validation such as ktlint when needed.
8. Review the final diff.
9. Confirm no temporary/debug code remains.
10. Confirm no secrets or local configuration were introduced.
11. Summarize what changed.
12. Report the exact validation commands that were executed.
13. Report unresolved issues, assumptions, or risks.

Never state that a task is fully complete when required validation could not be performed.

---

## 26. Agent Behavior

Prefer understanding over guessing.

Prefer existing QREZZY patterns over generic Android patterns.

Prefer focused changes over broad refactors.

Prefer simple solutions over unnecessary abstractions.

Prefer repository evidence over assumptions.

Before creating something new, search for an existing equivalent.

Do not "improve" unrelated code unless explicitly requested.

When uncertain:

1. inspect the repository,
2. inspect analogous code,
3. inspect tests,
4. inspect Gradle/configuration,
5. inspect CI,
6. then ask for clarification if the answer still cannot be determined.