package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    ENGLISH("en", "English", false),
    SINDHI("sd", "سنڌي", true),
    URDU("ur", "اردو", true)
}

object Localization {
    fun get(key: String, lang: AppLanguage): String {
        val en = when (key) {
            "app_title" -> "FaceMeets"
            "tagline" -> "Connect, Share & Discover"
            "feed" -> "Feed"
            "reels" -> "Reels"
            "camera" -> "Camera & Filters"
            "filters" -> "Snap Lenses & Filters"
            "create" -> "Create"
            "messages" -> "Messages"
            "activity" -> "Activity"
            "profile" -> "Profile"
            "search_hint" -> "Search people, tags, posts..."
            "create_post" -> "Create Post"
            "whats_on_mind" -> "What's on your mind?"
            "post_btn" -> "Publish Post"
            "feeling" -> "Feeling / Activity"
            "add_tags" -> "Add Tags (#)"
            "public" -> "Public"
            "friends" -> "Friends"
            "stories" -> "Stories"
            "your_story" -> "Your Story"
            "add_story" -> "Add to Story"
            "like" -> "Like"
            "comments" -> "Comments"
            "repost" -> "Repost"
            "share" -> "Share"
            "save" -> "Save"
            "write_comment" -> "Write a thoughtful comment..."
            "send" -> "Send"
            "reply" -> "Reply"
            "online" -> "Active Now"
            "trending" -> "Trending For You"
            "suggested_creators" -> "Suggested Creators"
            "follow" -> "Follow"
            "following" -> "Following"
            "followers" -> "Followers"
            "posts" -> "Posts"
            "likes" -> "Likes"
            "saved" -> "Saved"
            "edit_profile" -> "Edit Profile"
            "bio" -> "Bio"
            "name" -> "Name"
            "handle" -> "Handle"
            "save_changes" -> "Save Changes"
            "cancel" -> "Cancel"
            "dark_mode" -> "Dark Mode"
            "language" -> "Language"
            "all" -> "All"
            "mentions" -> "Mentions"
            "close" -> "Close"
            "shared_link" -> "Link copied to clipboard!"
            "reposted" -> "Post shared to your timeline!"
            "type_message" -> "Type a message..."
            "verified" -> "Verified"
            "sindhi_guide" -> "FaceMeets Guide (سنڌي)"
            "sindhi_guide_desc" -> "FaceMeets Android APK build & local prototype"
            "take_snap" -> "Take Snap"
            "share_story" -> "Add to Story"
            "share_post" -> "Share as Post"
            "save_gallery" -> "Save to Gallery"
            "intensity" -> "Filter Intensity"
            "add_caption_hint" -> "Add a snap caption..."
            "snap_saved" -> "Snap saved to device gallery!"
            "retake" -> "Retake"
            else -> key
        }

        val sd = when (key) {
            "app_title" -> "FaceMeets"
            "tagline" -> "ڳنڍجو، ونڊيو ۽ ڳوليو"
            "feed" -> "فيڊ"
            "reels" -> "ريلز"
            "camera" -> "ڪئميرا ۽ فلٽرز"
            "filters" -> "لينس ۽ فلٽرز"
            "create" -> "ٺاهيو"
            "messages" -> "پيغام"
            "activity" -> "سرگرميون"
            "profile" -> "پروفائل"
            "search_hint" -> "ماڻهو، ٽيگ، پوسٽون ڳوليو..."
            "create_post" -> "نئين پوسٽ ٺاهيو"
            "whats_on_mind" -> "اڄ اوهان جي دل ۾ ڇا آهي؟"
            "post_btn" -> "پوسٽ شايع ڪريو"
            "feeling" -> "احساس / سرگرمي"
            "add_tags" -> "ٽيگ شامل ڪريو (#)"
            "public" -> "عوامي"
            "friends" -> "دوست"
            "stories" -> "اسٽوريز"
            "your_story" -> "اوهان جي اسٽوري"
            "add_story" -> "اسٽوري لڳايو"
            "like" -> "پسند"
            "comments" -> "رايا"
            "repost" -> "ٻيهر ونڊيو"
            "share" -> "شيئر"
            "save" -> "محفوظ"
            "write_comment" -> "پنهنجو رايو لکو..."
            "send" -> "موڪليو"
            "reply" -> "جواب"
            "online" -> "آن لائين"
            "trending" -> "مشهور ترين"
            "suggested_creators" -> "تجويز ڪيل تخليقڪار"
            "follow" -> "فالو ڪريو"
            "following" -> "فالو ڪيل"
            "followers" -> "فالورز"
            "posts" -> "پوسٽون"
            "likes" -> "پسنديدگيون"
            "saved" -> "محفوظ ڪيل"
            "edit_profile" -> "پروفائل سنواريو"
            "bio" -> "تعارف"
            "name" -> "نالو"
            "handle" -> "هينڊل"
            "save_changes" -> "تبديليون محفوظ ڪريو"
            "cancel" -> "رد ڪريو"
            "dark_mode" -> "ڊارڪ موڊ"
            "language" -> "ٻولي"
            "all" -> "سڀ"
            "mentions" -> "ذڪر"
            "close" -> "بند ڪريو"
            "shared_link" -> "لنڪ ڪاپي ٿي ويو!"
            "reposted" -> "ٽائيم لائن تي ونڊيو ويو!"
            "type_message" -> "پيغام لکو..."
            "verified" -> "تصديق ٿيل"
            "sindhi_guide" -> "FaceMeets گائيڊ (سنڌي)"
            "sindhi_guide_desc" -> "FaceMeets Android APK build & local prototype"
            "take_snap" -> "سنيپ وٺو"
            "share_story" -> "اسٽوري ۾ وجھو"
            "share_post" -> "پوسٽ طور ونڊيو"
            "save_gallery" -> "گيلري ۾ محفوظ ڪريو"
            "intensity" -> "فلٽر جي شدت"
            "add_caption_hint" -> "سنيپ تي ڪجهه لکو..."
            "snap_saved" -> "تصوير گيلري ۾ محفوظ ٿي وئي!"
            "retake" -> "ٻيهر ڪلڪ ڪريو"
            else -> en
        }

        val ur = when (key) {
            "app_title" -> "FaceMeets"
            "tagline" -> "جڑیں، شئیر کریں اور دریافت کریں"
            "feed" -> "فیڈ"
            "reels" -> "ریلز"
            "camera" -> "کیمرہ اور فلٹرز"
            "filters" -> "لینسز اور فلٹرز"
            "create" -> "نیا بنائیں"
            "messages" -> "پیغامات"
            "activity" -> "اطلاعات"
            "profile" -> "پروفائل"
            "search_hint" -> "لوگ، ٹیگز، پوسٹس تلاش کریں..."
            "create_post" -> "نئی پوسٹ بنائیں"
            "whats_on_mind" -> "آپ کے ذہن میں کیا ہے؟"
            "post_btn" -> "پوسٹ لگائیں"
            "feeling" -> "احساس / سرگرمی"
            "add_tags" -> "ٹیگ شامل کریں (#)"
            "public" -> "عوامی"
            "friends" -> "دوست"
            "stories" -> "اسٹوریز"
            "your_story" -> "آپ کی اسٹوری"
            "add_story" -> "اسٹوری لگائیں"
            "like" -> "پسند"
            "comments" -> "تبصرے"
            "repost" -> "ری پوسٹ"
            "share" -> "شئیر"
            "save" -> "محفوظ"
            "write_comment" -> "اپنا تبصرہ لکھیں..."
            "send" -> "بھیجیں"
            "reply" -> "جواب دیں"
            "online" -> "آن لائن"
            "trending" -> "ٹرینڈنگ"
            "suggested_creators" -> "تجویز کردہ صارفین"
            "follow" -> "فالو کریں"
            "following" -> "فالو کر رہے ہیں"
            "followers" -> "فالوورز"
            "posts" -> "پوسٹس"
            "likes" -> "پسندیدگیاں"
            "saved" -> "محفوظ شدہ"
            "edit_profile" -> "پروفائل ایڈٹ کریں"
            "bio" -> "تعارف"
            "name" -> "نام"
            "handle" -> "ہینڈل"
            "save_changes" -> "محفوظ کریں"
            "cancel" -> "منسوخ"
            "dark_mode" -> "ڈارک موڈ"
            "language" -> "زبان"
            "all" -> "تمام"
            "mentions" -> "مینشنز"
            "close" -> "بند کریں"
            "shared_link" -> "لنک کاپی ہو گیا!"
            "reposted" -> "ٹائم لائن پر شیئر ہو گیا!"
            "type_message" -> "پیغام لکھیں..."
            "verified" -> "تصدیق شدہ"
            "sindhi_guide" -> "FaceMeets گائیڈ"
            "sindhi_guide_desc" -> "FaceMeets اینڈرائیڈ اے پی کے"
            "take_snap" -> "تصویر کھینچیں"
            "share_story" -> "اسٹوری میں لگائیں"
            "share_post" -> "پوسٹ بنائیں"
            "save_gallery" -> "گیلری میں محفوظ کریں"
            "intensity" -> "فلٹر کی شدت"
            "add_caption_hint" -> "کوئی بات لکھیں..."
            "snap_saved" -> "تصویر گیلری میں محفوظ ہو گئی!"
            "retake" -> "دوبارہ کھینچیں"
            else -> en
        }

        return when (lang) {
            AppLanguage.ENGLISH -> en
            AppLanguage.SINDHI -> sd
            AppLanguage.URDU -> ur
        }
    }
}

enum class ScreenTab {
    FEED,
    REELS,
    CAMERA,
    CREATE,
    MESSAGES,
    ACTIVITY,
    PROFILE
}

enum class SnapFilter(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val category: String,
    val description: String
) {
    ORIGINAL("original", "Normal", "✨", "Classic", "Original look"),
    BEAUTY_GLOW("beauty_glow", "Glow", "💖", "Beauty", "Soft radiance & blush"),
    NEON_CYBER("neon_cyber", "Cyberpunk", "⚡", "Cyber", "Electric cyan & magenta"),
    HEART_CROWN("heart_crown", "Heart Blush", "💕", "Cute", "Floating red & pink hearts"),
    CAT_EARS("cat_ears", "Kitty Ears", "🐱", "Cute", "Cute cat ears & whiskers"),
    RETRO_VHS("retro_vhs", "VHS 1998", "📼", "Retro", "Vintage grain & 90s stamp"),
    GOLDEN_HOUR("golden_hour", "Golden Hour", "🌅", "Vibe", "Warm sunset sunlight"),
    NOIR_CINEMA("noir_cinema", "B&W Cinema", "🎬", "Mood", "High contrast black & white"),
    SPARKLES("sparkles", "Sparkles", "🌟", "Glitter", "Twinkling star glitter"),
    ANIME_SKETCH("anime_sketch", "Comic Pop", "🎨", "Art", "Pop-art comic saturation"),
    DISCO_PARTY("disco_party", "Disco Party", "🪩", "Party", "Rainbow psychedelic hues")
}

data class CapturedSnap(
    val id: String,
    val imageRes: Int,
    val filter: SnapFilter,
    val filterIntensity: Float,
    val bannerCaption: String = "",
    val timestamp: String = "Just now"
)

data class User(
    val id: String,
    val name: String,
    val handle: String,
    val avatarBgColor: Long,
    val avatarInitial: String,
    val isVerified: Boolean = false,
    val bio: String = "",
    val location: String = "Karachi / Hyderabad, Sindh",
    val postsCount: Int = 18,
    val followersCount: Int = 1240,
    val followingCount: Int = 380,
    val isFollowing: Boolean = false
)

data class Comment(
    val id: String,
    val author: User,
    val text: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Post(
    val id: String,
    val author: User,
    val content: String,
    val imageRes: Int? = null,
    val imageLabel: String? = null,
    val timestamp: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val comments: List<Comment> = emptyList(),
    val repostsCount: Int = 0,
    val isReposted: Boolean = false,
    val isSaved: Boolean = false,
    val feeling: String? = null,
    val tags: List<String> = emptyList(),
    val isPinned: Boolean = false
)

data class Story(
    val id: String,
    val user: User,
    val text: String,
    val gradientColors: List<Long>,
    val timeAgo: String,
    val isViewed: Boolean = false,
    val imageRes: Int? = null
)

data class Reel(
    val id: String,
    val creator: User,
    val caption: String,
    val musicTrack: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val tags: List<String>,
    val gradientColors: List<Long>,
    val viewsCount: String = "24.5K"
)

data class Message(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean
)

data class Conversation(
    val id: String,
    val participant: User,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true,
    val messages: List<Message> = emptyList()
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION
}

data class NotificationItem(
    val id: String,
    val actor: User,
    val type: NotificationType,
    val text: String,
    val timeAgo: String,
    val isRead: Boolean = false
)
