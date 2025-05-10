package com.ethiopianairlines.pilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ethiopianairlines.pilot.ui.theme.PilotTheme
import com.ethiopianairlines.pilot.presentation.login.LoginScreen
import com.ethiopianairlines.pilot.presentation.signup.SignUpScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Entry point: LoginScreen (presentation.login package) for Pilot Preparation App
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PilotTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onSignUpClick = { navController.navigate("signup") }
                        )
                    }
                    composable("signup") {
                        SignUpScreen(
                            onLoginClick = { navController.popBackStack("login", inclusive = false) }
                        )
                    }
                }
            }
        }
    }
}