package com.example.data

import com.example.R
import com.example.model.*

object SampleData {
    val currentUser = User(
        id = "u_me",
        name = "Parwar Morai",
        handle = "parwarmorai",
        avatarBgColor = 0xFF00E5FF,
        avatarInitial = "P",
        isVerified = true,
        bio = "📱 Passionate App Developer & Community Builder | Building FaceMeets next-gen social experience 🚀",
        location = "Sindh, Pakistan",
        postsCount = 24,
        followersCount = 3840,
        followingCount = 412,
        isFollowing = false
    )

    val friendSarah = User(
        id = "u_1",
        name = "Sarah Khan",
        handle = "sarahk_design",
        avatarBgColor = 0xFF9D4EDD,
        avatarInitial = "S",
        isVerified = true,
        bio = "Lead UI/UX Designer & Mobile Architect",
        location = "Karachi, Pakistan",
        isFollowing = true
    )

    val friendZubair = User(
        id = "u_2",
        name = "Zubair Ahmed",
        handle = "zubair_dev",
        avatarBgColor = 0xFF00D2C4,
        avatarInitial = "Z",
        isVerified = false,
        bio = "Android Engineer | Kotlin & Compose enthusiast",
        location = "Hyderabad, Sindh",
        isFollowing = true
    )

    val friendAyesha = User(
        id = "u_3",
        name = "Ayesha Noor",
        handle = "ayesha_tech",
        avatarBgColor = 0xFFFF2A85,
        avatarInitial = "A",
        isVerified = true,
        bio = "Tech Speaker | AI Researcher | Community Lead",
        location = "Lahore, Pakistan",
        isFollowing = false
    )

    val friendDanish = User(
        id = "u_4",
        name = "Danish Ali",
        handle = "danish_photog",
        avatarBgColor = 0xFFFFB703,
        avatarInitial = "D",
        isVerified = false,
        bio = "Sunset Hunter & Cityscape Photographer 📸",
        location = "Islamabad, Pakistan",
        isFollowing = true
    )

    fun initialStories(): List<Story> = listOf(
        Story(
            id = "s_0",
            user = currentUser,
            text = "Welcome to FaceMeets APK! 🎉",
            gradientColors = listOf(0xFF00F2FE, 0xFF4FACFE),
            timeAgo = "Just now",
            isViewed = false
        ),
        Story(
            id = "s_1",
            user = friendSarah,
            text = "Redesigning social media interactions with fluid motion! 🎨✨",
            gradientColors = listOf(0xFF9D4EDD, 0xFFFF2A85),
            timeAgo = "1h ago",
            isViewed = false
        ),
        Story(
            id = "s_2",
            user = friendZubair,
            text = "Native Jetpack Compose is lightning fast! ⚡",
            gradientColors = listOf(0xFF00D2C4, 0xFF0052D4),
            timeAgo = "3h ago",
            isViewed = false
        ),
        Story(
            id = "s_3",
            user = friendDanish,
            text = "Sunset walk by the harbor — city lights waking up 🌆",
            gradientColors = listOf(0xFFFF7E5F, 0xFFFEB47B),
            timeAgo = "5h ago",
            isViewed = true,
            imageRes = R.drawable.img_post_sample_1792568126760
        ),
        Story(
            id = "s_4",
            user = friendAyesha,
            text = "Community meetup announcement coming soon! 📢",
            gradientColors = listOf(0xFF11998E, 0xFF38EF7D),
            timeAgo = "7h ago",
            isViewed = true
        )
    )

    fun initialPosts(): List<Post> = listOf(
        Post(
            id = "p_1",
            author = currentUser,
            content = "🌟 FaceMeets Android APK is live! Built with 100% native Kotlin Jetpack Compose, edge-to-edge dark theme, stories, reels, instant messaging, and bilingual English/Sindhi/Urdu support. Connect freely and explore!",
            imageRes = R.drawable.img_cover_banner_1792568107879,
            imageLabel = "FaceMeets Next Gen Banner",
            timestamp = "10m ago",
            likesCount = 142,
            isLiked = true,
            commentsCount = 19,
            repostsCount = 34,
            isReposted = false,
            isSaved = true,
            feeling = "🚀 Excited",
            tags = listOf("FaceMeets", "Android", "Kotlin", "SindhTech"),
            isPinned = true,
            comments = listOf(
                Comment(
                    id = "c_1",
                    author = friendSarah,
                    text = "The dark indigo & cyan design looks phenomenal! Incredible work on the fluid UI 🔥",
                    timestamp = "8m ago",
                    likesCount = 12,
                    isLiked = true
                ),
                Comment(
                    id = "c_2",
                    author = friendZubair,
                    text = "سٺو ڪم پرور صاحب! سنڌي سپورٽ واقعي بهترين اضافو آهي (Great job Parwar! The Sindhi support is wonderful) 👏",
                    timestamp = "5m ago",
                    likesCount = 9,
                    isLiked = false
                )
            )
        ),
        Post(
            id = "p_2",
            author = friendDanish,
            content = "Golden hour vibes in the coastal metropolis. The evening glow reflecting across the tech hub skyline is pure magic 🌇✨ Which skyline is your favorite?",
            imageRes = R.drawable.img_post_sample_1792568126760,
            imageLabel = "Sunset Harbor View",
            timestamp = "2h ago",
            likesCount = 389,
            isLiked = false,
            commentsCount = 42,
            repostsCount = 58,
            isSaved = false,
            feeling = "📸 Inspired",
            tags = listOf("Photography", "SunsetVibes", "CityLights", "Travel"),
            comments = listOf(
                Comment(
                    id = "c_3",
                    author = currentUser,
                    text = "Spectacular shot Danish! The neon reflections in the water are stunning.",
                    timestamp = "1h ago",
                    likesCount = 15,
                    isLiked = true
                )
            )
        ),
        Post(
            id = "p_3",
            author = friendAyesha,
            content = "💡 Quick thought: Social networks thrive when communities feel respected, empowered, and truly connected without algorithmic noise. Let's build spaces where authentic voices shine! #CommunityFirst #TechForGood",
            timestamp = "4h ago",
            likesCount = 215,
            isLiked = false,
            commentsCount = 28,
            repostsCount = 76,
            feeling = "💡 Thoughtful",
            tags = listOf("CommunityFirst", "AuthenticVoices", "TechForGood"),
            comments = emptyList()
        )
    )

    fun initialReels(): List<Reel> = listOf(
        Reel(
            id = "r_1",
            creator = currentUser,
            caption = "Building FaceMeets mobile experience with slick animations & dark indigo vibes 🚀📱",
            musicTrack = "FaceMeets Beats • Neon Flow (Original)",
            likesCount = 1240,
            isLiked = true,
            commentsCount = 88,
            tags = listOf("androiddev", "compose", "facemeets", "tech"),
            gradientColors = listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364),
            viewsCount = "18.2K"
        ),
        Reel(
            id = "r_2",
            creator = friendSarah,
            caption = "3 tips for modern micro-interactions that will make your app feel buttery smooth! 🎨✨",
            musicTrack = "Lo-Fi Lounge • Midnight Coffee",
            likesCount = 3450,
            isLiked = false,
            commentsCount = 152,
            tags = listOf("design", "uiux", "prototyping", "creative"),
            gradientColors = listOf(0xFF4A00E0, 0xFF8E2DE2),
            viewsCount = "45.1K"
        ),
        Reel(
            id = "r_3",
            creator = friendDanish,
            caption = "Timelapse: Harbor city dusk transition from gold to vibrant neon blues 🎥🌆",
            musicTrack = "Synthwave Dreams • Night Drive",
            likesCount = 5890,
            isLiked = true,
            commentsCount = 310,
            tags = listOf("timelapse", "cityscapes", "nightlife", "cinematic"),
            gradientColors = listOf(0xFF1A1A2E, 0xFF16213E, 0xFF0F3460),
            viewsCount = "72.4K"
        )
    )

    fun initialConversations(): List<Conversation> = listOf(
        Conversation(
            id = "conv_1",
            participant = friendSarah,
            lastMessage = "Hey Parwar! The new FaceMeets UI layout looks sharp!",
            lastTimestamp = "12:45 PM",
            unreadCount = 1,
            isOnline = true,
            messages = listOf(
                Message("m1", "u_1", "Hey Parwar! Have you tested the new responsive layout?", "12:30 PM", false),
                Message("m2", "u_me", "Yes Sarah! It works on both phones and tablets now.", "12:35 PM", true),
                Message("m3", "u_1", "Hey Parwar! The new FaceMeets UI layout looks sharp!", "12:45 PM", false)
            )
        ),
        Conversation(
            id = "conv_2",
            participant = friendZubair,
            lastMessage = "ڀاءُ APK زبردست ٺهي آهي! (Brother the APK built wonderfully!)",
            lastTimestamp = "Yesterday",
            unreadCount = 0,
            isOnline = true,
            messages = listOf(
                Message("m4", "u_2", "سلام پرور ڀاءُ، نئون فيچر ٽيسٽ ڪيو؟", "Yesterday", false),
                Message("m5", "u_me", "ها زبير! هاڻي هي بلڪل تيار آهي.", "Yesterday", true),
                Message("m6", "u_2", "ڀاءُ APK زبردست ٺهي آهي! (Brother the APK built wonderfully!)", "Yesterday", false)
            )
        ),
        Conversation(
            id = "conv_3",
            participant = friendAyesha,
            lastMessage = "Let's plan the weekend developer meetup!",
            lastTimestamp = "Oct 19",
            unreadCount = 0,
            isOnline = false,
            messages = listOf(
                Message("m7", "u_3", "Let's plan the weekend developer meetup!", "Oct 19", false)
            )
        )
    )

    fun initialNotifications(): List<NotificationItem> = listOf(
        NotificationItem(
            id = "n_1",
            actor = friendSarah,
            type = NotificationType.LIKE,
            text = "liked your post: \"FaceMeets Android APK is live!\"",
            timeAgo = "5m ago",
            isRead = false
        ),
        NotificationItem(
            id = "n_2",
            actor = friendZubair,
            type = NotificationType.COMMENT,
            text = "commented: \"سٺو ڪم پرور صاحب! سنڌي سپورٽ واقعي بهترين اضافو آهي\"",
            timeAgo = "12m ago",
            isRead = false
        ),
        NotificationItem(
            id = "n_3",
            actor = friendAyesha,
            type = NotificationType.FOLLOW,
            text = "started following you.",
            timeAgo = "1h ago",
            isRead = true
        ),
        NotificationItem(
            id = "n_4",
            actor = friendDanish,
            type = NotificationType.MENTION,
            text = "mentioned you in a comment on a photo.",
            timeAgo = "3h ago",
            isRead = true
        )
    )

    val suggestedCreators: List<User> = listOf(friendSarah, friendZubair, friendAyesha, friendDanish)

    val trendingTopics: List<Pair<String, String>> = listOf(
        "#FaceMeets" to "42.8K posts",
        "#SindhTech" to "18.3K posts",
        "#AndroidDev" to "85.1K posts",
        "#KotlinCompose" to "29.4K posts",
        "#KarachiMeetup" to "12.7K posts"
    )
}
