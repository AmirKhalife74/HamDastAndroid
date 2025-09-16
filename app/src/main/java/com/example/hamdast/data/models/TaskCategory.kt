package com.example.hamdast.data.models

enum class TaskCategory(val displayName: String, val emoji: String) {
    HEALTH("سلامت", "🏃‍♂️"),
    LEARNING("یادگیری و رشد", "📚"),
    WORK("کار و وظایف", "💼"),
    SOCIAL("روابط و اجتماع", "🤝"),
    PERSONAL("شخصی و تفریح", "🎨")
}