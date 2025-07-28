# WhatsApp Timer - Auto Call Hang-up

A private Android application that automatically hangs up WhatsApp voice calls after a specified timer expires.

## Features

- ⏱️ **Countdown Timer**: Set a timer in minutes for automatic call termination
- 📱 **WhatsApp Integration**: Specifically designed to work with WhatsApp voice calls
- 🔧 **Accessibility Service**: Uses Android Accessibility Service for automated interactions
- 🎨 **Modern UI**: Clean, Material Design interface with WhatsApp-inspired colors
- 🔒 **Privacy-Focused**: No data collection, works entirely offline

## Requirements

- Android 7.0 (API level 24) or higher
- WhatsApp installed on the device
- Accessibility Service permissions (for call automation)

## Installation

### Option 1: Download from GitHub Actions

1. Go to the [Actions tab](../../actions) in this repository
2. Click on the latest successful build
3. Download the `app-debug.apk` artifact
4. Install the APK on your Android device

### Option 2: Build from Source

1. Clone this repository:
   ```bash
   git clone <repository-url>
   cd whatsapp-timer
   ```

2. Build the APK:
   ```bash
   ./gradlew assembleDebug
   ```

3. The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`

## Usage

1. **Install and Launch**: Install the APK and open the WhatsApp Timer app
2. **Set Timer**: Enter the desired time in minutes (e.g., 5 for 5 minutes)
3. **Start Timer**: Tap "START TIMER" to begin the countdown
4. **Make WhatsApp Call**: Start your WhatsApp voice call as normal
5. **Automatic Hang-up**: When the timer expires, the app will attempt to hang up the call
6. **Stop Timer**: Use "STOP TIMER" to cancel the countdown at any time

## Setup for WhatsApp Automation

To enable automatic call hang-up functionality:

1. **Enable Accessibility Service**:
   - Go to Settings → Accessibility
   - Find "WhatsApp Timer" service
   - Turn it ON
   - Grant permission when prompted

2. **Grant Permissions**:
   - The app may request additional permissions for system overlay and accessibility
   - These are required for automated call interactions

## Technical Details

### Architecture
- **Language**: Kotlin
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)
- **UI Framework**: Material Design Components

### Key Components
- `MainActivity.kt`: Main timer interface and logic
- `WhatsAppAccessibilityService.kt`: Handles automated WhatsApp interactions
- `TimerService.kt`: Background service for timer functionality (optional)

### Automation Methods
The app uses multiple approaches to detect and interact with WhatsApp calls:

1. **Accessibility Service**: Monitors WhatsApp UI elements and simulates button clicks
2. **Text Recognition**: Looks for "hang up", "end call", "decline" buttons
3. **Resource ID Detection**: Searches for specific WhatsApp button IDs
4. **Content Description Matching**: Uses accessibility labels to find call controls

## Limitations

- **WhatsApp Updates**: WhatsApp UI changes may affect automation reliability
- **Device Variations**: Different Android versions/manufacturers may behave differently
- **Permission Requirements**: Requires accessibility permissions which some users may not want to grant
- **Call Screen Detection**: May not work with all WhatsApp call screen variations

## Privacy & Security

- ✅ **No Internet Access**: App works entirely offline
- ✅ **No Data Collection**: No personal information is collected or transmitted
- ✅ **Limited Scope**: Accessibility service only monitors WhatsApp package
- ✅ **Open Source**: Full source code available for review

## Troubleshooting

### Timer Not Working
- Ensure the app has proper permissions
- Check that accessibility service is enabled
- Verify WhatsApp is installed and updated

### Call Not Hanging Up
- Make sure accessibility service is running
- Try restarting the accessibility service in Settings
- Check that WhatsApp call screen is visible when timer expires

### Build Issues
- Ensure you have JDK 17+ installed
- Update Android SDK to latest version
- Run `./gradlew clean` before building

## Development

### Project Structure
```
app/
├── src/main/
│   ├── java/com/example/whatsapptimer/
│   │   ├── MainActivity.kt
│   │   ├── WhatsAppAccessibilityService.kt
│   │   └── TimerService.kt
│   ├── res/
│   │   ├── layout/activity_main.xml
│   │   ├── values/strings.xml
│   │   ├── values/colors.xml
│   │   └── xml/accessibility_service_config.xml
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

### Building
```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing)
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Disclaimer

This application is for personal use only. The developers are not responsible for any misuse of this application. Users should ensure they comply with local laws and WhatsApp's terms of service when using automated tools.

## Support

If you encounter issues or have questions:

1. Check the [Issues](../../issues) page for existing solutions
2. Create a new issue with detailed information about your problem
3. Include your Android version, device model, and WhatsApp version

---

**Note**: This is an unofficial application and is not affiliated with WhatsApp Inc.