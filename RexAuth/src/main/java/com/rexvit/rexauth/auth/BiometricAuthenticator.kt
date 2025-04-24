package com.rexvit.rexauth.auth


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.utils.Constants
import java.util.concurrent.Executor

class BiometricAuthenticator(
    private val context: Context,
    private val authConfig: AuthConfig,
    private val callback: AuthCallback
) {

    interface AuthCallback {
        fun onSuccess()
        fun onError(errorCode: Int, errorMessage: String)
        fun onHelp(helpCode: Int, helpMessage: String)
    }

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    fun authenticate(activity: FragmentActivity) {
        executor = ContextCompat.getMainExecutor(context)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Use your biometric credential")
            .apply {
                if (authConfig.biometricStrength == Constants.BIOMETRIC_STRONG) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        setAllowedAuthenticators(
                            BiometricManager.Authenticators.BIOMETRIC_STRONG
                        )
                    } else {
                        setNegativeButtonText("Use PIN/Pattern")
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        setAllowedAuthenticators(
                            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    } else {
                        setNegativeButtonText("Use PIN/Pattern")
                    }
                }

                if (authConfig.allowDeviceCredentials) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        setAllowedAuthenticators(
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    } else {
                        setDeviceCredentialAllowed(true)
                    }
                }
            }
            .build()

        biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.onError(errorCode, errString.toString())
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    callback.onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.onError(-1, "Authentication failed")
                }
            })

        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricPrompt.authenticate(promptInfo)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                callback.onError(-1, "No biometric features available")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                callback.onError(-1, "Biometric features currently unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                callback.onError(-1, "No biometric credentials enrolled")
            else -> callback.onError(-1, "Unknown error")
        }
    }

    fun cancelAuthentication() {
        if (::biometricPrompt.isInitialized) {
            biometricPrompt.cancelAuthentication()
        }
    }
}