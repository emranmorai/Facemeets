# FaceMeets — Modern Flutter Starter

A responsive social-app prototype for Android and Flutter Web, with a premium dark-indigo / teal visual system.

## What's upgraded
- Responsive navigation: bottom navigation on mobile and a navigation rail on wider screens.
- Redesigned home feed, hero panel, story rings, search, post cards and reaction controls.
- Local-session post creation, like toggles, comment sheet and copy-to-clipboard sharing.
- Dedicated Discover/Reels preview, Messages, Activity/Notifications and Profile screens.
- Empty search state, basic input length limit and clearer demo-only messaging.
- No third-party packages required by the current UI prototype.

## Run locally
1. Install the Flutter SDK: https://docs.flutter.dev/get-started/install
2. In this folder run `flutter pub get`
3. Run Android: `flutter run`
4. Run web: `flutter run -d chrome` (enable web support in Flutter if needed)
5. Build Android APK: `flutter build apk --release`
6. Build web release: `flutter build web --release`

## Backend status — read before publishing
This project is still a **front-end prototype**, not a production social network. Posts and likes live only in app memory; conversations, activity, stories, and profile stats are illustrative sample content. Real authentication, durable posts, media uploads, real-time chat, push notifications and moderation are not yet connected.

## Suggested Firebase backend implementation
1. Create a Firebase project and register Android and Web apps.
2. Configure FlutterFire using the official CLI (`dart pub global activate flutterfire_cli`, then `flutterfire configure`). Keep generated platform config in the project as instructed by Firebase.
3. Add and initialize `firebase_core`, `firebase_auth`, `cloud_firestore`, `firebase_storage`, and `firebase_messaging` as required.
4. Implement email/password or approved identity-provider authentication; add account recovery and profile setup.
5. Store posts and comments in Firestore; store images/videos in Cloud Storage; enforce ownership and validation with Firestore/Storage Security Rules.
6. Use a trusted server/Cloud Functions for push notifications and any AI API calls. Never ship service-account credentials or private API keys in the client app.
7. Add reporting, blocking, rate limits, content moderation, privacy controls, account deletion and age-appropriate safeguards before public launch.
8. Test rules with the Firebase Emulator Suite and verify Android + web behavior before release.

Backend integration is intentionally not faked: real Firebase configuration and security rules must be supplied and tested before enabling live account/data features.
