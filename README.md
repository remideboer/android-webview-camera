# Android Camera App

This Android app embeds the HTML camera page in a WebView, allowing you to use it as a replacement for the camera app.

## Setup Instructions

1. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `android-app` folder

2. **Copy HTML file:**
   - Copy `index.html` from the parent directory to `android-app/app/src/main/assets/index.html`
   - Or the app will load it from assets automatically

3. **Build and Run:**
   - Connect your Android device (enable USB debugging)
   - Click "Run" in Android Studio
   - The app will install and launch on your device

## Features

- Full-screen WebView with camera access
- No browser UI - just your camera interface
- Works offline after first load
- Camera permissions handled by Android
- Photos can be saved to device storage

## Permissions

The app requests:
- `CAMERA` - Required for camera access
- `WRITE_EXTERNAL_STORAGE` - For saving photos (Android 10+ may not need this)
- `READ_EXTERNAL_STORAGE` - For accessing saved photos

## Notes

- WebView treats `file:///android_asset/` as a secure context, so camera APIs will work
- The HTML page will automatically detect it's running in WebView and work properly
- No HTTPS server needed - everything runs locally in the app

