# Cattle App

A modern, multilingual Android application designed for cattle breed identification and management. Built with Jetpack Compose and Material 3, Cattle App empowers users to identify cattle breeds instantly using their camera, manually register livestock, and maintain comprehensive records—all in their preferred language.

## Overview

Cattle App combines cutting-edge mobile technology with practical livestock management needs. Whether you're a farmer, veterinarian, or livestock enthusiast, this app provides an intuitive interface for breed identification and animal record-keeping across multiple languages.

## Key Features

### Breed Identification
Identify cattle breeds instantly using advanced image recognition:
- **Camera Integration**: Take a picture directly from your phone's camera for real-time breed prediction
- **Gallery Selection**: Choose existing images from your device gallery for analysis
- **Instant Results**: Get immediate breed identification with detailed information

### Manual Registration
Comprehensive animal registration with detailed record-keeping:
- **Complete Information Form**: Register animals with tag number, species, breed, name, and date of birth
- **Custom Fields**: Capture all essential data for proper livestock management
- **Easy Data Entry**: Intuitive form design makes registration quick and efficient

### Past Records
Access and manage your complete livestock database:
- **Comprehensive List View**: Browse all previously registered cattle in one place
- **Easy Management**: View, search, and organize your animal records
- **Historical Tracking**: Maintain a complete history of your livestock

### User Profile
Personalized user experience with profile management:
- **User Information**: View your name, location, and user ID
- **Account Management**: Secure logout functionality
- **Profile Overview**: Quick access to your account details

### Multilingual Support
Full internationalization for broader accessibility:
- **Three Languages**: Support for English, Hindi, and Odia
- **Language Selection**: Choose your preferred language at login
- **Complete Localization**: Both static UI text and dynamic backend data are translated
- **Seamless Switching**: Language preference is maintained throughout the app

### Modern, Themed UI
Beautiful and consistent user interface design:
- **100% Jetpack Compose**: Built entirely with modern declarative UI toolkit
- **Material 3 Design**: Follows the latest Material Design guidelines
- **Custom Fonts**: Carefully selected typography for enhanced readability
- **Themed Backgrounds**: Consistent visual theme across all screens
- **Responsive Layouts**: Optimized for various screen sizes and orientations

## Tech Stack & Architecture

### UI Framework
- **Jetpack Compose**: Modern declarative UI toolkit for Android
- **Material 3**: Latest Material Design components and theming

### Architecture Pattern
- **MVVM (Model-View-ViewModel)**: Clean separation of concerns for maintainable code
- **Repository Pattern**: Abstraction layer for data sources

### Dependency Management
- **Hilt**: Compile-time dependency injection for Android

### Networking
- **Retrofit**: Type-safe HTTP client for API communication
- **OkHttp**: Efficient HTTP client with interceptor support
- **Language Interceptor**: Custom interceptor for handling multilingual API requests

### Asynchronous Programming
- **Kotlin Coroutines**: Simplified asynchronous programming
- **Flow**: Reactive streams for data handling

### Navigation
- **Jetpack Navigation for Compose**: Type-safe navigation between composables

## Prerequisites

Before you begin, ensure you have the following installed:
- Android Studio (latest version recommended)
- JDK 8 or higher
- Android SDK with minimum API level 24 (Android 7.0)
- Git

## Setup Instructions

Follow these steps to get the project up and running:

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/cattle-app.git
cd cattle-app
```

### 2. Configure API Base URL

Create a `local.properties` file in the root directory of the project (if it doesn't already exist):

```bash
touch local.properties
```

Add the backend API's base URL to the `local.properties` file:

```properties
BASE_URL="https://your.api.base.url/"
```

**Note**: Replace `https://your.api.base.url/` with your actual backend API endpoint.

### 3. Open in Android Studio

1. Launch Android Studio
2. Select **File → Open**
3. Navigate to the cloned project directory and select it
4. Wait for Gradle to sync all dependencies

### 4. Run the Application

1. Connect an Android device via USB or start an Android emulator
2. Click the **Run** button in Android Studio or press `Shift + F10`
3. Select your target device
4. The app will build and launch on your selected device

## Project Structure

```
cattle-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/yourpackage/cattleapp/
│   │   │   │   ├── data/          # Data layer (repositories, models)
│   │   │   │   ├── di/            # Dependency injection modules
│   │   │   │   ├── network/       # API services and interceptors
│   │   │   │   ├── ui/            # UI layer (composables, screens)
│   │   │   │   ├── viewmodel/     # ViewModels
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── values/        # English strings
│   │   │   │   ├── values-hi/     # Hindi strings
│   │   │   │   └── values-or/     # Odia strings
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── local.properties
└── README.md
```

## Configuration

### Language Support

The app supports three languages out of the box:
- **English** (default)
- **Hindi** (हिंदी)
- **Odia** (ଓଡ଼ିଆ)

Language selection is available on the login screen and persists throughout the user session.

### Network Configuration

The app uses OkHttp interceptors to automatically include language headers in API requests, ensuring that backend responses are properly localized.

---

**Note**: This application requires an active backend API connection for full functionality. Ensure your API endpoint is properly configured in `local.properties` before running the app.
