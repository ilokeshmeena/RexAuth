package com.rexvit.rexauth.ui

import android.app.Application
import android.graphics.Color
import com.rexvit.rexauth.auth.RexAuthManager
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.config.ThemeConfig
import com.rexvit.rexauth.utils.Constants

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val authConfig = AuthConfig(
            authTypes = listOf(Constants.AUTH_TYPE_BIOMETRIC, Constants.AUTH_TYPE_PIN),
            defaultAuthType = Constants.AUTH_TYPE_BIOMETRIC,
            lockTimeoutMs = 10 * 60 * 1000L, // 10 minutes
            maxAttempts = 5,
            biometricStrength = Constants.BIOMETRIC_STRONG,
            autoLockOnBackground = true
        )

        val themeConfig = ThemeConfig(
            primaryColor = Color.parseColor("#4CAF50"),
            primaryDarkColor = Color.parseColor("#388E3C"),
            accentColor = Color.parseColor("#8BC34A"),
            textColor = Color.WHITE,
            errorColor = Color.RED,
            backgroundColor = Color.parseColor("#303030"),
            title = "My App Security",
            subtitle = "Authenticate to continue"
        )

        RexAuthManager.initialize(this, authConfig, themeConfig)
    }
}