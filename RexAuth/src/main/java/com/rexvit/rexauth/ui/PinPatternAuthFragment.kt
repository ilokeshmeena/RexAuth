package com.rexvit.rexauth.ui

import com.example.rexauth.R
import com.example.rexauth.databinding.FragmentPinPatternAuthBinding
import com.rexvit.rexauth.auth.PinPatternAuthenticator
import com.rexvit.rexauth.config.AuthConfig
import com.rexvit.rexauth.config.ThemeConfig
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goodiebag.pinview.Pinview

class PinPatternAuthFragment : Fragment() {

    private var _binding: FragmentPinPatternAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var authConfig: AuthConfig
    private lateinit var themeConfig: ThemeConfig
    private var isPinAuth: Boolean = true
    private lateinit var authenticator: PinPatternAuthenticator

    companion object {
        fun newInstance(
            authConfig: AuthConfig,
            themeConfig: ThemeConfig,
            isPin: Boolean
        ): PinPatternAuthFragment {
            return PinPatternAuthFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("auth_config", authConfig)
                    putParcelable("theme_config", themeConfig)
                    putBoolean("is_pin", isPin)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinPatternAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authConfig = arguments?.getParcelable("auth_config") ?: AuthConfig()
        themeConfig = arguments?.getParcelable("theme_config") ?: ThemeConfig()
        isPinAuth = arguments?.getBoolean("is_pin") ?: true

        setupUI()
        initAuthenticator()
    }

    private fun setupPinView() {
        binding.pinView.setPinViewEventListener(object : Pinview.PinViewEventListener {
            override fun onDataEntered(pinview: Pinview?, fromUser: Boolean) {
                pinview?.value.let {
                    if (it != null) {
                        authenticator.verifyPin(it, "1234")
                    }
                }
            }
        })
    }

    private fun resetPinView() {
        binding.pinView.value = "" // Clears the PIN input
    }

//    private fun setupPatternView() {
//        binding.patternView.addPatternLockListener(object : PatternLockViewListener {
//            override fun onStarted() {}
//            override fun onProgress(progressPattern: List<Dot>) {}
//
//            override fun onComplete(pattern: List<Dot>) {
//                val patternString = pattern.joinToString("") { it.row.toString() + it.column.toString() }
//                authenticator.verifyPattern(patternString, getStoredPattern())
//            }
//
//            override fun onCleared() {}
//        })
//    }

    private fun getStoredPattern(): String {
        // Implement your pattern storage logic
        return "1234" // Example pattern
    }


    private fun setupUI() {
        binding.title.setTextColor(themeConfig.textColor)
        binding.subtitle.setTextColor(themeConfig.textColor)
        binding.errorText.setTextColor(themeConfig.errorColor)

        if (isPinAuth) {
            binding.title.text = themeConfig.title ?: getString(R.string.pin_auth_title)
            binding.subtitle.text = themeConfig.subtitle ?: getString(R.string.pin_auth_subtitle)
            binding.pinView.visibility = View.VISIBLE
            binding.patternView.visibility = View.GONE
        } else {
            binding.title.text = themeConfig.title ?: getString(R.string.pattern_auth_title)
            binding.subtitle.text = themeConfig.subtitle ?: getString(R.string.pattern_auth_subtitle)
            binding.pinView.visibility = View.GONE
            binding.patternView.visibility = View.VISIBLE
        }

        // Setup PIN or Pattern listeners
        if (isPinAuth) {
            setupPinView()
        } else {
//            binding.patternView.setPatternListener(object : PatternLockView.OnPatternListener {
//                override fun onPatternComplete(pattern: List<Int>) {
//                    val patternString = pattern.joinToString("")
//                    if (authenticator.verifyPattern(patternString, storedPattern)
//                }
//            })
        }
    }

    private fun initAuthenticator() {
        authenticator = PinPatternAuthenticator(
            requireContext(),
            authConfig,
            object : PinPatternAuthenticator.AuthCallback {
                override fun onSuccess() {
                    (activity as? RexAuthActivity)?.onAuthSuccess()
                }

                override fun onError(errorMessage: String) {
                    binding.errorText.text = errorMessage
                    binding.errorText.visibility = View.VISIBLE
                    if (isPinAuth) {
                        resetPinView()
                    } else {
//                        binding.patternView.setViewMode(
//                            com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG
//                        )
//                        binding.patternView.clearPattern()
                    }
                }

                override fun onAttemptsRemaining(attempts: Int) {
                    binding.errorText.text = getString(R.string.attempts_remaining, attempts)
                    binding.errorText.visibility = View.VISIBLE
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}