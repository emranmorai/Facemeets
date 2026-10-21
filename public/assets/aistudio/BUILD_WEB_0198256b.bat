@echo off
setlocal
cd /d "%~dp0"
echo ========================================
echo FaceMeets - Web Build
echo ========================================
where flutter >nul 2>nul
if errorlevel 1 (
  echo ERROR: Flutter command not found.
  echo Install Flutter and add its bin folder to Windows PATH.
  echo Guide: https://docs.flutter.dev/get-started/install/windows
  pause
  exit /b 1
)
flutter config --enable-web
if errorlevel 1 goto failed
flutter pub get
if errorlevel 1 goto failed
flutter build web --release
if errorlevel 1 goto failed
echo.
echo SUCCESS! Web files are in:
echo %CD%\build\web
explorer "%CD%\build\web"
pause
exit /b 0
:failed
echo Web build failed. Read the error above.
pause
exit /b 1
