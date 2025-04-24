package com.rexvit.rexauth.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.rexauth.R
import com.example.rexauth.databinding.ActivityRexAuthBinding
import com.rexvit.rexauth.auth.BiometricAuthenticator
import com.rexvit.rexauth.auth.PinPatternAuthenticator
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.config.ThemeConfig
import com.rexvit.rexauth.utils.Constants

class RexAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRexAuthBinding
    private lateinit var authConfig: AuthConfig
    private lateinit var themeConfig: ThemeConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        binding = ActivityRexAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        authConfig = intent.getParcelableExtra("auth_config") ?: AuthConfig()
        themeConfig = intent.getParcelableExtra("theme_config") ?: ThemeConfig()

        applyTheme()
        showInitialAuthFragment()
    }

    private fun applyTheme() {
        window.statusBarColor = themeConfig.primaryDarkColor
        window.navigationBarColor = themeConfig.backgroundColor

        binding.root.setBackgroundColor(themeConfig.backgroundColor)
        // Apply other theme configurations to UI elements
    }

    private fun showInitialAuthFragment() {
        when (authConfig.defaultAuthType) {
            Constants.AUTH_TYPE_BIOMETRIC -> showBiometricAuthFragment()
            Constants.AUTH_TYPE_PIN -> showPinPatternAuthFragment(isPin = true)
            Constants.AUTH_TYPE_PATTERN -> showPinPatternAuthFragment(isPin = false)
        }
    }

    private fun showBiometricAuthFragment() {
        supportFragmentManager.commit {
            replace(R.id.auth_container, BiometricAuthFragment.newInstance(authConfig, themeConfig))
        }
    }

    private fun showPinPatternAuthFragment(isPin: Boolean) {
        supportFragmentManager.commit {
            replace(
                R.id.auth_container,
                PinPatternAuthFragment.newInstance(authConfig, themeConfig, isPin)
            )
        }
    }

    fun onAuthSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    fun onFallbackToPinPattern(isPin: Boolean) {
        showPinPatternAuthFragment(isPin)
    }
}