package com.rexvit.rexauth.config

import android.os.Parcelable
import com.rexvit.rexauth.utils.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthConfig(
    val authTypes: List<Int> = listOf(
        Constants.AUTH_TYPE_BIOMETRIC,
        Constants.AUTH_TYPE_PIN
    ),
    val defaultAuthType: Int = Constants.AUTH_TYPE_BIOMETRIC,
    val lockTimeoutMs: Long = Constants.DEFAULT_LOCK_TIMEOUT,
    val maxAttempts: Int = Constants.DEFAULT_MAX_ATTEMPTS,
    val biometricStrength: Int = Constants.BIOMETRIC_STRONG,
    val allowDeviceCredentials: Boolean = true,
    val autoLockOnBackground: Boolean = true,
    val showLockScreenOnLaunch: Boolean = true
): Parcelable