@echo off
setlocal
cd /d "%~dp0"
echo ========================================
echo FaceMeets - Android APK Build
echo ========================================
where flutter >nul 2>nul
if errorlevel 1 (
  echo ERROR: Flutter command not found.
  echo Please install Flutter and add its bin folder to Windows PATH.
  echo Guide: https://docs.flutter.dev/get-started/install/windows
  pause
  exit /b 1
)
echo Checking Flutter setup...
flutter doctor
if errorlevel 1 (
  echo Flutter doctor reported a problem. Fix Android SDK setup before building.
  pause
  exit /b 1
)
echo Downloading project packages...
flutter pub get
if errorlevel 1 goto failed
echo Building release APK...
flutter build apk --release
if errorlevel 1 goto failed
echo.
echo SUCCESS! APK path:
echo %CD%\build\app\outputs\flutter-apk\app-release.apk
explorer "%CD%\build\app\outputs\flutter-apk"
pause
exit /b 0
:failed
echo.
echo Build failed. Read the error shown above and fix the missing dependency or SDK setup.
pause
exit /b 1
