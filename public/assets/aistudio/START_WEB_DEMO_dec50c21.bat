@echo off
setlocal
cd /d "%~dp0"
where flutter >nul 2>nul
if errorlevel 1 (
  echo Flutter command not found. Install Flutter first.
  pause
  exit /b 1
)
flutter config --enable-web
flutter pub get
if errorlevel 1 pause & exit /b 1
flutter run -d chrome
pause
