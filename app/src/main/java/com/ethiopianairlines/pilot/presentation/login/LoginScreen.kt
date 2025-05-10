package com.ethiopianairlines.pilot.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ethiopianairlines.pilot.R
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), onSignUpClick: () -> Unit = {}, onForgotPasswordClick: () -> Unit = {}) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.success.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_signup),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Foreground content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(24.dp)
                    .fillMaxWidth(0.95f)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Login",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.email.value = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.password.value = it },
                    label = { Text("password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { viewModel.login() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(48.dp)
                    ) {
                        Text("Login", fontWeight = FontWeight.Bold)
                    }
                }
                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(error ?: "", color = Color.Red, fontSize = 14.sp)
                }
                if (success) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Login successful!", color = Color.Green, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onForgotPasswordClick) {
                        Text("Forgot Password?", color = Color.White, fontSize = 12.sp)
                    }
                    TextButton(onClick = onSignUpClick) {
                        Text("Don't have an account?signup", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
} 