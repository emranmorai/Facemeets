# FaceMeets — سنڌي ۾ آسان گائيڊ

هي شروعاتي Flutter پروجيڪٽ آهي. ان کي Android، Web ۽ iOS لاءِ اڳتي وڌائي سگهجي ٿو. هن وقت اهو **ڊيمو UI** آهي، مڪمل آن لائين سوشل نيٽ ورڪ ناهي.

## Windows ڪمپيوٽر تي پهريون ڀيرو تياري

1. Flutter جي سرڪاري هدايتن مان Windows لاءِ Flutter SDK انسٽال ڪريو: https://docs.flutter.dev/get-started/install/windows
2. Android Studio انسٽال ڪريو ۽ ان ۾ Android SDK انسٽال/سيٽ اپ ڪريو: https://developer.android.com/studio
3. Flutter جي `bin` فولڊر کي Windows PATH ۾ شامل ڪريو (سرڪاري Flutter گائيڊ موجب).
4. ڪمپيوٽر تي نئون Command Prompt کوليو، پوءِ هن فولڊر ۾ وڃو جتي `pubspec.yaml` موجود آهي.
5. هي ڪمانڊ هلايو: `flutter doctor`
6. جيڪڏهن Android licenses بابت چيو وڃي ته `flutter doctor --android-licenses` هلايو ۽ اسڪرين تي ڏنل سوالن جو جواب ڏيو.

## APK ٺاهڻ جو آسان طريقو

1. FaceMeets پروجيڪٽ جو ZIP Extract ڪريو.
2. Extract ٿيل فولڊر اندر `pubspec.yaml` واري فولڊر ۾ وڃو.
3. `BUILD_ANDROID_APK.bat` تي ڊبل ڪلڪ ڪريو.
4. پهريون ڀيرو انٽرنيٽ ۽ SDK ڊائون لوڊ ٿيڻ سبب وقت لڳي سگهي ٿو.
5. ڪاميابي سان ٺهڻ بعد APK هن جڳهه تي هوندي: `build\app\outputs\flutter-apk\app-release.apk`
6. APK کي پنهنجي Android فون ۾ منتقل ڪري انسٽال ڪريو. فون ۾ صرف پنهنجي ٺاهيل/ڀروسي واري APK انسٽال ڪريو؛ Android شايد اجازت گهري.

## ويب ورزن ٺاهڻ

`BUILD_WEB.bat` تي ڊبل ڪلڪ ڪريو. ويب فائلون `build\web` فولڊر ۾ ٺهنديون. ويب سائيٽ طور آن لائين رکڻ لاءِ پوءِ hosting جي ضرورت پوندي.

## iPhone / iOS بابت

Windows تي سڌو iOS ايپ compile/sign نٿي ڪري سگهجي. iOS build لاءِ Mac ۽ Xcode استعمال ڪريو، يا معتبر remote/cloud Mac build service وٺو. App Store تي شايع ڪرڻ لاءِ Apple جي اڪائونٽ ۽ پاليسين جي گهرجن کي به پورو ڪرڻو پوندو.

## هن وقت ڇا ڪم ڪري ٿو؟

- Dark + Blue ڊزائن، نموني جون پوسٽون ۽ Stories
- ڊيمو پوسٽ ٺاهڻ، Like ۽ Search
- ڪجهه اسڪرينون صرف ڏيک/placeholder آهن

## اڃا ڇا ٺاهڻو آهي؟

حقيقي اڪائونٽ/لاگ اِن، آن لائين database، مختلف فونن وچ ۾ پوسٽون، فوٽو/وڊيو upload، محفوظ chat، دوست/فالو، notifications، AI سروس، Groups/Pages، Marketplace، Live streaming، رپورٽنگ/بلاڪنگ ۽ رازداري/سيڪيورٽي ٽيسٽ.

**خبردار:** هي ZIP پاڻمرادو تيار APK ناهي؛ هي source project آهي جنهن مان APK ٺاهڻ لاءِ Flutter ۽ Android SDK انسٽال ٿيل هئڻ گهرجن.
