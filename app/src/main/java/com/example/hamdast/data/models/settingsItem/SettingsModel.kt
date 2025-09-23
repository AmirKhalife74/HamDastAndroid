package com.example.hamdast.data.models.settingsItem

import android.graphics.drawable.Drawable

data class SettingsModel(
    var title: String,
    var subTitle: String,
    var imgSrc: Int,
    var onClick: () -> Unit
)
