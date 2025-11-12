# Camera App - ASUS Zenfone 8 Workaround

A simple Android app created as a workaround for a known issue causing the ASUS Zenfone 8 camera app to stop working. This tool restores basic camera functionality until the official fix is released.

## Overview

This Android app provides a WebView-based camera interface that works around the camera app issues on ASUS Zenfone 8 devices. While the native camera app may fail, the browser's camera API still functions correctly, allowing this app to capture photos and save them to your device.

## The Problem

Many ASUS Zenfone 8 users have reported that the native camera app stops working, displaying a black screen or failing to function entirely. However, the camera hardware itself is functional - third-party apps like WhatsApp, MS Teams, and browser-based camera interfaces can still access the camera successfully.

This indicates a software issue with the native ASUS camera app, not a hardware problem. Common symptoms include:

- Camera app shows black screen
- Camera app crashes on launch
- Camera app fails to initialize
- Error messages when trying to use the camera

**Why This App Works:** While the native camera app fails, Android's browser camera API (`getUserMedia`) continues to function correctly. This app uses a WebView to access the camera through the browser API, bypassing the problematic native camera app entirely.

## Known Workarounds

Before using this app, you may want to try these official troubleshooting steps:

1. **Check App Permissions** - Settings > Apps > Camera > Permissions
2. **Clear Cache and Data** - Settings > Apps > Camera > Storage & Cache
3. **Update Software** - Check for system and app updates
4. **Safe Mode Test** - Boot into Safe Mode to check for third-party interference
5. **Hardware Test** - Use SMMI Test (Calculator app: type `.12345+=`)

If these steps don't resolve the issue, this app provides a reliable workaround until ASUS releases an official fix.

## Features

- ✅ **Automatic camera startup** - Camera ready immediately when app opens
- ✅ **Photo capture** - Large, easy-to-use capture button (camera app style)
- ✅ **Automatic saving** - Photos saved directly to Pictures directory
- ✅ **Photo deletion** - Delete last captured photo if needed
- ✅ **QR code scanning** - Built-in QR code scanner functionality
- ✅ **Offline capable** - Works without internet connection
- ✅ **Full-screen interface** - Clean, distraction-free camera view

## Requirements

- Android 7.0 (API 24) or higher
- ASUS Zenfone 8 (or any Android device with camera)
- Camera permission granted

## Installation

### Option 1: Build from Source

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd android-app
   ```

2. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `android-app` folder

3. **Build and Install:**
   - Connect your Android device (enable USB debugging)
   - Click "Run" in Android Studio
   - The app will build and install on your device

### Option 2: Install APK (if available)

Download the latest APK from the [Releases](../../releases) page and install on your device.

## Usage

1. **Launch the app** - The camera will start automatically
2. **Grant permissions** - Allow camera access when prompted (first time only)
3. **Take photos** - Tap the large round capture button
4. **Photos are saved automatically** - Check your Pictures folder
5. **Delete if needed** - Use the "Delete photo" button to remove the last photo

## How It Works

This app uses a WebView to load an HTML-based camera interface. The browser's camera API (`getUserMedia`) continues to work even when the native camera app fails, providing a reliable workaround for the ASUS Zenfone 8 camera issue.

- **WebView-based** - Uses Android WebView for camera access
- **Native file saving** - Photos saved via Android MediaStore API
- **Secure context** - WebView provides secure context for camera APIs
- **No server required** - Everything runs locally on your device

## Permissions

The app requires the following permissions:

- **CAMERA** - Required for camera access
- **WRITE_EXTERNAL_STORAGE** - For saving photos (Android 9 and below)
- **READ_EXTERNAL_STORAGE** - For accessing saved photos (Android 9 and below)

*Note: Android 10+ uses scoped storage and doesn't require explicit storage permissions.*

## Troubleshooting

### Camera doesn't start
- Ensure camera permission is granted in Android Settings
- Restart the app
- Check that no other app is using the camera

### Photos not saving
- Verify storage permissions are granted (Android 9 and below)
- Check available storage space
- Photos are saved to: `Pictures/photo_YYYYMMDD_HHMMSS.jpg`

### App crashes
- Ensure you're running Android 7.0 or higher
- Try clearing app data and restarting
- Report the issue with device details

## Technical Details

- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Architecture:** WebView + Native Android APIs
- **File Format:** JPEG (92% quality)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

```
Copyright [YEAR] [COPYRIGHT HOLDER]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Disclaimer

This app is provided as-is as a temporary workaround for the ASUS Zenfone 8 camera issue. It is not affiliated with ASUS. Use at your own discretion.

## Acknowledgments

Created to help ASUS Zenfone 8 users maintain camera functionality while waiting for an official fix.
