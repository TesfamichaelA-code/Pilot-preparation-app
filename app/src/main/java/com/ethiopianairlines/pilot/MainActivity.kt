package com.ethiopianairlines.pilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ethiopianairlines.pilot.ui.theme.PilotTheme
import com.ethiopianairlines.pilot.presentation.login.LoginScreen
import com.ethiopianairlines.pilot.presentation.signup.SignUpScreen
import com.ethiopianairlines.pilot.presentation.admin.AdminHomeScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import com.ethiopianairlines.pilot.navigation.NavGraph

// Entry point: LoginScreen (presentation.login package) for Pilot Preparation App
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PilotTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}