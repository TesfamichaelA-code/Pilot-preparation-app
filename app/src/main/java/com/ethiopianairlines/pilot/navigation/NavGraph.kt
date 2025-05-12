package com.ethiopianairlines.pilot.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ethiopianairlines.pilot.presentation.login.LoginScreen
import com.ethiopianairlines.pilot.presentation.signup.SignUpScreen
import com.ethiopianairlines.pilot.presentation.admin.AdminHomeScreen
import com.ethiopianairlines.pilot.presentation.admin.exams.AddEditQuestionsScreen
import com.ethiopianairlines.pilot.presentation.admin.exams.ExamScreen
import com.ethiopianairlines.pilot.presentation.admin.exams.ExamListScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object AdminHome : Screen("admin_home")
    object ExamList : Screen("exam_list") // <-- Add this for ExamListScreen
    object AddExam : Screen("add_exam") // This will be your ExamScreen
    object EditExamQuestions : Screen("edit_exam_questions/{examId}") { // New route for questions
        fun createRoute(examId: String) = "edit_exam_questions/$examId"
    }
    // You can add other admin screens here if needed (e.g., Interview, Progress, Resources)
    // object AdminInterview : Screen("admin_interview")
    // object AdminProgress : Screen("admin_progress")
    // object AdminResources : Screen("admin_resources")
}


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSignUpClick = {
                    navController.navigate(Screen.Signup.route)
                },
                onLoginSuccess = { isAdmin ->
                    // Assuming isAdmin is true for simplicity, or handle user roles appropriately
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignUpScreen(
                onLoginClick = {
                    navController.popBackStack() // Go back to Login screen
                }
            )
        }

        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                onExamsClick = {
                    // Navigate to ExamListScreen when "Exams" is clicked
                    navController.navigate(Screen.ExamList.route)
                },
                // Add other navigation lambdas if AdminHomeScreen has more buttons
                onInterviewClick = { /* navController.navigate(Screen.AdminInterview.route) */ },
                onProgressClick = { /* navController.navigate(Screen.AdminProgress.route) */ },
                onResourcesClick = { /* navController.navigate(Screen.AdminResources.route) */ }
            )
        }


        composable(Screen.ExamList.route) {
            ExamListScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddExamClick = {
                    navController.navigate(Screen.AddExam.route)
                },
                onNavigateToEditQuestions = { examId ->
                    // Navigate to the screen for adding/editing questions
                    navController.navigate(Screen.EditExamQuestions.createRoute(examId))
                }
            )
        }

        composable(Screen.AddExam.route) {
            // This is your existing ExamScreen for creating the exam shell
            com.ethiopianairlines.pilot.presentation.admin.exams.ExamScreen( // Use fully qualified name if ambiguous
                onNavigateBack = { navController.popBackStack() },
                onExamCreated = {
                    navController.navigate(Screen.ExamList.route) {
                        popUpTo(Screen.AddExam.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // New composable for the "Add/Edit Questions" screen
        composable(
            route = Screen.EditExamQuestions.route,
            arguments = listOf(navArgument("examId") { type = NavType.StringType })
        ) { backStackEntry ->
            val examId = backStackEntry.arguments?.getString("examId")
            if (examId != null) {
                // Replace with your actual Add/Edit Questions Screen Composable
                // This screen will be similar to your second screenshot
                AddEditQuestionsScreen( // You'll need to create this Composable
                    examId = examId,
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                // Handle error: examId not found (should not happen if route is correct)
                Text("Error: Exam ID not found.")
            }
        }
    }
}
