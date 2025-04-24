package com.rexvit.rexauth.auth

import android.content.Context
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.storage.AuthPreferences
import com.rexvit.rexauth.utils.Constants
import javax.crypto.Cipher

class PinPatternAuthenticator(
    private val context: Context,
    private val authConfig: AuthConfig,
    private val callback: AuthCallback
) {

    interface AuthCallback {
        fun onSuccess()
        fun onError(errorMessage: String)
        fun onAttemptsRemaining(attempts: Int)
    }

    private val authPrefs by lazy { AuthPreferences(context) }

    fun verifyPin(pin: String, storedPin: String) {
        if (authPrefs.isLocked()) {
            callback.onError("Account locked. Try again later.")
            return
        }

        if (pin == storedPin) {
            authPrefs.resetAttempts()
            callback.onSuccess()
        } else {
            handleFailedAttempt()
        }
    }

    fun verifyPattern(pattern: String, storedPattern: String) {
        if (authPrefs.isLocked()) {
            callback.onError("Account locked. Try again later.")
            return
        }

        if (pattern == storedPattern) {
            authPrefs.resetAttempts()
            callback.onSuccess()
        } else {
            handleFailedAttempt()
        }
    }

    fun setupPin(pin: String, cipher: Cipher? = null) {
        // Store encrypted PIN
        // Implementation in AuthPreferences
    }

    fun setupPattern(pattern: String, cipher: Cipher? = null) {
        // Store encrypted Pattern
        // Implementation in AuthPreferences
    }

    private fun handleFailedAttempt() {
        authPrefs.incrementAttempts()
        val remaining = authConfig.maxAttempts - authPrefs.getCurrentAttempts()

        if (remaining > 0) {
            callback.onAttemptsRemaining(remaining)
        } else {
            authPrefs.lockAccount()
            callback.onError("Too many attempts. Account locked for ${Constants.LOCK_DURATION / 1000} seconds.")
        }
    }
}