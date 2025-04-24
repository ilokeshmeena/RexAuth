package com.rexvit.rexauth.utils

object Constants {
    const val PREF_NAME = "rex_auth_prefs"
    const val KEY_LAST_ACTIVE_TIME = "last_active_time"
    const val KEY_LOCK_TIMEOUT = "lock_timeout"
    const val KEY_AUTH_TYPE = "auth_type"
    const val KEY_MAX_ATTEMPTS = "max_attempts"
    const val KEY_CURRENT_ATTEMPTS = "current_attempts"
    const val KEY_IS_LOCKED = "is_locked"
    const val KEY_LOCK_UNTIL = "lock_until"

    const val AUTH_TYPE_BIOMETRIC = 1
    const val AUTH_TYPE_PIN = 2
    const val AUTH_TYPE_PATTERN = 3

    const val DEFAULT_LOCK_TIMEOUT = 5 * 60 * 1000L // 5 minutes
    const val DEFAULT_MAX_ATTEMPTS = 5
    const val LOCK_DURATION = 30 * 1000L // 30 seconds

    const val BIOMETRIC_STRONG = 1
    const val BIOMETRIC_WEAK = 2
}