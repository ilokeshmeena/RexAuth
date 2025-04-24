package com.rexvit.rexauth.storage

import android.content.Context
import android.content.SharedPreferences
import com.rexvit.rexauth.security.CryptoManager
import com.rexvit.rexauth.utils.Constants
import java.util.concurrent.TimeUnit

class AuthPreferences(context: Context) {

    private val cryptoManager = CryptoManager(context)
    private val sharedPreferences = cryptoManager.getEncryptedSharedPreferences(
        Constants.PREF_NAME,
        context
    )

    fun setLastActiveTime() {
        sharedPreferences.edit()
            .putLong(Constants.KEY_LAST_ACTIVE_TIME, System.currentTimeMillis())
            .apply()
    }

    fun shouldShowLockScreen(lockTimeout: Long = Constants.DEFAULT_LOCK_TIMEOUT): Boolean {
        val lastActiveTime = sharedPreferences.getLong(Constants.KEY_LAST_ACTIVE_TIME, 0L)
        return System.currentTimeMillis() - lastActiveTime > lockTimeout
    }

    fun lockNow() {
        sharedPreferences.edit()
            .putLong(Constants.KEY_LAST_ACTIVE_TIME, 0L)
            .apply()
    }

    fun unlock() {
        setLastActiveTime()
    }

    fun isLocked(): Boolean {
        val lockUntil = sharedPreferences.getLong(Constants.KEY_LOCK_UNTIL, 0L)
        return lockUntil > System.currentTimeMillis()
    }

    fun resetAttempts() {
        sharedPreferences.edit()
            .putInt(Constants.KEY_CURRENT_ATTEMPTS, 0)
            .putLong(Constants.KEY_LOCK_UNTIL, 0L)
            .apply()
    }

    fun incrementAttempts() {
        val current = sharedPreferences.getInt(Constants.KEY_CURRENT_ATTEMPTS, 0)
        sharedPreferences.edit()
            .putInt(Constants.KEY_CURRENT_ATTEMPTS, current + 1)
            .apply()
    }

    fun getCurrentAttempts(): Int {
        return sharedPreferences.getInt(Constants.KEY_CURRENT_ATTEMPTS, 0)
    }

    fun lockAccount() {
        sharedPreferences.edit()
            .putLong(Constants.KEY_LOCK_UNTIL, System.currentTimeMillis() + Constants.LOCK_DURATION)
            .apply()
    }

    fun setAuthType(authType: Int) {
        sharedPreferences.edit()
            .putInt(Constants.KEY_AUTH_TYPE, authType)
            .apply()
    }

    fun getCurrentAuthType(): Int {
        return sharedPreferences.getInt(Constants.KEY_AUTH_TYPE, Constants.AUTH_TYPE_BIOMETRIC)
    }

    fun getRemainingLockTime(): Long {
        val lockUntil = sharedPreferences.getLong(Constants.KEY_LOCK_UNTIL, 0L)
        return if (lockUntil > System.currentTimeMillis()) {
            lockUntil - System.currentTimeMillis()
        } else {
            0L
        }
    }

    fun formatRemainingLockTime(): String {
        val millis = getRemainingLockTime()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }
}