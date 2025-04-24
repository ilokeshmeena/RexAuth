package com.rexvit.rexauth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.rexauth.R
import com.example.rexauth.databinding.FragmentBiometricAuthBinding
import com.rexvit.rexauth.auth.BiometricAuthenticator
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.config.ThemeConfig
import com.rexvit.rexauth.utils.Constants

class BiometricAuthFragment : Fragment() {

    private var _binding: FragmentBiometricAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var authConfig: AuthConfig
    private lateinit var themeConfig: ThemeConfig
    private lateinit var biometricAuthenticator: BiometricAuthenticator

    companion object {
        fun newInstance(authConfig: AuthConfig, themeConfig: ThemeConfig): BiometricAuthFragment {
            return BiometricAuthFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("auth_config", authConfig)
                    putParcelable("theme_config", themeConfig)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiometricAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authConfig = arguments?.getParcelable("auth_config") ?: AuthConfig()
        themeConfig = arguments?.getParcelable("theme_config") ?: ThemeConfig()

        setupUI()
        initBiometricAuth()
    }

    private fun setupUI() {
        binding.title.setTextColor(themeConfig.textColor)
        binding.subtitle.setTextColor(themeConfig.textColor)
        binding.fallbackButton.setTextColor(themeConfig.accentColor)

        binding.title.text = themeConfig.title ?: getString(R.string.biometric_auth_title)
        binding.subtitle.text = themeConfig.subtitle ?: getString(R.string.biometric_auth_subtitle)

        binding.fallbackButton.setOnClickListener {
            (activity as? RexAuthActivity)?.onFallbackToPinPattern(
                authConfig.authTypes.contains(Constants.AUTH_TYPE_PIN)
            )
        }
    }

    private fun initBiometricAuth() {
        biometricAuthenticator = BiometricAuthenticator(
            requireContext(),
            authConfig,
            object : BiometricAuthenticator.AuthCallback {
                override fun onSuccess() {
                    (activity as? RexAuthActivity)?.onAuthSuccess()
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    binding.errorText.text = errorMessage
                    binding.errorText.setTextColor(themeConfig.errorColor)
                    binding.errorText.visibility = View.VISIBLE
                }

                override fun onHelp(helpCode: Int, helpMessage: String) {
                    binding.errorText.text = helpMessage
                    binding.errorText.visibility = View.VISIBLE
                }
            })

        (activity as? FragmentActivity)?.let {
            biometricAuthenticator.authenticate(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        biometricAuthenticator.cancelAuthentication()
        _binding = null
    }
}