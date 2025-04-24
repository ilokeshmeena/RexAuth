package com.rexvit.rexauth.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.config.ThemeConfig
import com.rexvit.rexauth.ui.RexAuthActivity
import com.rexvit.rexauth.utils.Constants

class RexAuthManager private constructor(
    private val context: Context,
    private val authConfig: AuthConfig,
    private val themeConfig: ThemeConfig
) {

    companion object {
        private var instance: RexAuthManager? = null

        fun initialize(context: Context, authConfig: AuthConfig, themeConfig: ThemeConfig) {
            if (instance == null) {
                instance = RexAuthManager(context.applicationContext, authConfig, themeConfig)
            }
        }

        fun getInstance(): RexAuthManager {
            return instance ?: throw IllegalStateException("RexAuthManager not initialized. Call initialize() first.")
        }
    }

    fun authenticate(): ActivityResultContract<Unit, Boolean> {
        return object : ActivityResultContract<Unit, Boolean>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(context, RexAuthActivity::class.java).apply {
                    putExtra("auth_config", authConfig)
                    putExtra("theme_config", themeConfig)
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                return resultCode == Activity.RESULT_OK
            }
        }
    }

    fun setLastActiveTime() {
        // Implementation in AuthPreferences
    }

    fun shouldShowLockScreen(): Boolean {
        // Implementation in AuthPreferences
        return true
    }

    fun lockNow() {
        // Implementation in AuthPreferences
    }

    fun unlock() {
        // Implementation in AuthPreferences
    }

    fun isLocked(): Boolean {
        // Implementation in AuthPreferences
        return false
    }

    fun resetAttempts() {
        // Implementation in AuthPreferences
    }

    fun incrementAttempts() {
        // Implementation in AuthPreferences
    }

    fun getRemainingAttempts(): Int {
        // Implementation in AuthPreferences
        return authConfig.maxAttempts
    }

    fun setAuthType(authType: Int) {
        // Implementation in AuthPreferences
    }

    fun getCurrentAuthType(): Int {
        // Implementation in AuthPreferences
        return authConfig.defaultAuthType
    }
}