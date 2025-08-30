# MemorizeWords - Android App

A simple and effective Android app to help you memorize words and improve your vocabulary in different languages.

## Features

- Create custom word lists
- Learn words in different modes
- Track your progress
- Edit and manage your lists
- **Light/Dark theme support** ✨
- **Multi-language support** 🌍

## Theme Support

The app now includes comprehensive theme support with three options:

### Available Themes

1. **Light Theme** - Clean, bright interface perfect for daytime use
2. **Dark Theme** - Easy on the eyes, ideal for low-light environments
3. **System Default** - Automatically follows your device's system theme setting

### How to Change Themes

1. Open the app
2. Navigate to **Settings** from the home screen
3. In the **Theme** section, select your preferred theme:
   - Tap the radio button next to "Light Theme" for a bright interface
   - Tap the radio button next to "Dark Theme" for a dark interface
   - Tap the radio button next to "System Default" to follow your device settings
4. The theme will be applied immediately and saved for future app launches

### Theme Features

- **Persistent Settings**: Your theme choice is saved and remembered between app sessions
- **Visual Preview**: See a preview of how each theme looks in the settings
- **Dynamic Colors**: On Android 12+ devices, the app supports dynamic color schemes
- **Smooth Transitions**: Theme changes are applied instantly without app restart

## Language Support

The app now supports **11 languages** to provide a localized experience for users worldwide:

### Supported Languages

1. **English** - Default language
2. **Türkçe** - Turkish
3. **Bahasa Indonesia** - Indonesian
4. **中文** - Chinese
5. **Español** - Spanish
6. **العربية** - Arabic
7. **हिन्दी** - Hindi
8. **Português** - Portuguese
9. **Français** - French
10. **Русский** - Russian
11. **বাংলা** - Bengali

### How to Change Language

1. Open the app
2. Navigate to **Settings** from the home screen
3. In the **Language** section, select your preferred language
4. The language will be applied immediately and saved for future app launches

### Language Features

- **Complete Localization**: All app text, buttons, and messages are translated
- **Persistent Settings**: Your language choice is saved and remembered between app sessions
- **Native Language Names**: Languages are displayed in their native script
- **RTL Support**: Full support for right-to-left languages like Arabic

## Technical Details

- Built with **Jetpack Compose**
- Uses **Material Design 3** components
- Theme and language preferences stored in **SharedPreferences**
- Supports both light and dark color schemes
- Compatible with Android 6.0 (API 23) and above
- **Multi-language localization** with Android's built-in resource system

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run on your device or emulator
4. Navigate to Settings to customize your theme and language preferences

## Contributing

Feel free to contribute to this project by:
- Reporting bugs
- Suggesting new features
- Submitting pull requests
- Adding translations for new languages

## Localization

To add support for a new language:

1. Create a new `values-[language_code]` folder in `app/src/main/res/`
2. Add a `strings.xml` file with translated strings
3. Update the `AppLanguage` enum in `LanguageManager.kt`
4. Add the language display name to the `getLanguageDisplayName` function

Example for adding German support:
```kotlin
enum class AppLanguage(val code: String, val displayName: String) {
    // ... existing languages
    GERMAN("de", "Deutsch")
}
```

## License

This project is open source and available under the [MIT License](LICENSE).
