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
import com.ethiopianairlines.pilot.presentation.home.HomeScreen
import com.ethiopianairlines.pilot.presentation.exams.ExamsScreen
import com.ethiopianairlines.pilot.presentation.exams.ExamDetailScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object AdminHome : Screen("admin_home")
    object StudentHome : Screen("student_home") // New route for student home
    object ExamList : Screen("exam_list")
    object AddExam : Screen("add_exam")
    object EditExamQuestions : Screen("edit_exam_questions/{examId}") {
        fun createRoute(examId: String) = "edit_exam_questions/$examId"
    }
    object Interview : Screen("interview")
    object Progress : Screen("progress") // Added for student navigation
    object Resources : Screen("resources") // Added for student navigation
    object StudentExams : Screen("student_exams") // New route for student exams
    object ExamDetail : Screen("exam_detail/{examId}") { // For viewing a specific exam
        fun createRoute(examId: String) = "exam_detail/$examId"
    }
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
                    // Navigate to appropriate home screen based on user role
                    val destination = if (isAdmin) Screen.AdminHome.route else Screen.StudentHome.route
                    navController.navigate(destination) {
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

        // Admin Home Screen
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                onExamsClick = {
                    navController.navigate(Screen.ExamList.route)
                },
                onInterviewClick = {
                    navController.navigate(Screen.Interview.route)
                },
                onProgressClick = { /* navController.navigate(Screen.AdminProgress.route) */ },
                onResourcesClick = { /* navController.navigate(Screen.AdminResources.route) */ }
            )
        }
        
        // Student Home Screen
        composable(Screen.StudentHome.route) {
            HomeScreen(
                onExamsClick = {
                    navController.navigate(Screen.StudentExams.route)
                },
                onInterviewClick = {
                    // Navigate to student interview practice
                    // navController.navigate(Screen.StudentInterview.route)
                },
                onProgressClick = {
                    // Navigate to student progress tracking
                    // navController.navigate(Screen.Progress.route)
                },
                onResourcesClick = {
                    // Navigate to resources screen
                    // navController.navigate(Screen.Resources.route)
                }
            )
        }
        
        // Student Exams Screen
        composable(Screen.StudentExams.route) {
            ExamsScreen(
                onNavigateBack = { navController.popBackStack() },
                onExamSelected = { examId ->
                    navController.navigate(Screen.ExamDetail.createRoute(examId))
                }
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

        composable(Screen.Interview.route) {
            com.ethiopianairlines.pilot.presentation.admin.interview.AddInterviewScreen()
        }
        
        // Exam Detail Screen (for taking an exam)
        composable(
            route = Screen.ExamDetail.route,
            arguments = listOf(navArgument("examId") { type = NavType.StringType })
        ) { backStackEntry ->
            val examId = backStackEntry.arguments?.getString("examId")
            if (examId != null) {
                ExamDetailScreen(
                    examId = examId,
                    onNavigateBack = { navController.popBackStack() },
                    onStartExam = { /* TODO: Implement taking exam */ }
                )
            } else {
                Text("Error: Exam ID not found.")
            }
        }
    }
}
