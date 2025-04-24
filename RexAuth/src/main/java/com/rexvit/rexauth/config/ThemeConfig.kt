package com.rexvit.rexauth.config

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ThemeConfig(
    @ColorInt val primaryColor: Int = Color.parseColor("#6200EE"),
    @ColorInt val primaryDarkColor: Int = Color.parseColor("#3700B3"),
    @ColorInt val accentColor: Int = Color.parseColor("#03DAC5"),
    @ColorInt val textColor: Int = Color.parseColor("#FFFFFF"),
    @ColorInt val errorColor: Int = Color.parseColor("#FF0000"),
    @ColorInt val backgroundColor: Int = Color.parseColor("#121212"),
    val logoResId: Int? = null,
    val title: String? = "Secure Authentication",
    val subtitle: String? = "Verify your identity to continue"
): Parcelable