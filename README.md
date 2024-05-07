# Road Fighter Compose - Kotlin Multiplatform Port

This project is an attempt to port the [Road Fighter Compose](https://github.com/kaiwalyakhasnis/Road-Fighter-Compose) game into Kotlin Multiplatform.
Kotlin Multiplatform is leveraged in this project to share both business logic and UI components across both platforms.
<img src="https://github.com/kaiwalyakhasnis/KMPRoadFighrer/blob/main/output.gif" width="240" height="400">

## State Management with Mozilla State Library

This project utilizes the [Mozilla State Library](https://github.com/mozilla-mobile/firefox-android/tree/main/android-components/components/lib/state) for Redux-style state management. The library provides a predictable state container for managing application state across different parts of the app.

### Port to Kotlin Multiplatform

The Mozilla State Library, originally designed for Android, has been ported to Kotlin Multiplatform in this project. This allows us to use the same state management solution across various platforms, including Android, iOS, and potentially others.

### Features

- Redux-style state management with actions, reducers, and a single immutable state tree.
- Middleware support for handling asynchronous actions and side effects.
- Integration with Kotlin Multiplatform, enabling cross-platform compatibility.

## TODO

Here are some tasks that need to be completed for the project:

- [ ] Make store subscription Compose lifecycle aware
- [ ] Refactor `observeAsComposableState`
