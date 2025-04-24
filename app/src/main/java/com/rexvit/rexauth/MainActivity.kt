package com.rexvit.rexauth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rexvit.rexauth.auth.RexAuthManager
import com.rexvit.rexauth.ui.theme.RexAuthTheme

class MainActivity : ComponentActivity() {
    private val authLauncher = registerForActivityResult(
        RexAuthManager.getInstance().authenticate()
    ) { success ->
        if (success) {
            // Authentication successful
            Toast.makeText(this, "Authentication Done", Toast.LENGTH_SHORT).show()

        } else {
            // Authentication failed
            Toast.makeText(this, "Authentication required", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RexAuthTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        if (RexAuthManager.getInstance().shouldShowLockScreen()) {
            authLauncher.launch(Unit)
        }
    }

    override fun onPause() {
        super.onPause()
        RexAuthManager.getInstance().setLastActiveTime()
    }}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RexAuthTheme {
        Greeting("Android")
    }
}