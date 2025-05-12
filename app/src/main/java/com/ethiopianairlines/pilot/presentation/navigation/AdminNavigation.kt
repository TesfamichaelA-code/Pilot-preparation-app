package com.ethiopianairlines.pilot.presentation.navigation // Ensure this is the correct package

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ethiopianairlines.pilot.presentation.admin.AdminHomeScreen
import com.ethiopianairlines.pilot.presentation.admin.exams.ExamListScreen
import com.ethiopianairlines.pilot.presentation.admin.exams.ExamScreen

sealed class AdminScreen(val route: String) {
    object Home : AdminScreen("admin_home")
    object ExamList : AdminScreen("admin_exam_list")
    object AddExam : AdminScreen("admin_add_exam")
    object Interview : AdminScreen("admin_interview") // Assuming these are still needed
    object Progress : AdminScreen("admin_progress")
    object Resources : AdminScreen("admin_resources")
}

@Composable
fun AdminNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AdminScreen.Home.route // "admin_home"
    ) {
        composable(AdminScreen.Home.route) {
            AdminHomeScreen(
                onExamsClick = { navController.navigate(AdminScreen.ExamList.route) },
                onInterviewClick = { navController.navigate(AdminScreen.Interview.route) },
                onProgressClick = { navController.navigate(AdminScreen.Progress.route) },
                onResourcesClick = { navController.navigate(AdminScreen.Resources.route) }
            )
        }

        composable(AdminScreen.ExamList.route) {
            ExamListScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddExamClick = { navController.navigate(AdminScreen.AddExam.route) },
                onNavigateToEditQuestions = TODO(),
                viewModel = TODO()
            )
        }

        composable(AdminScreen.AddExam.route) {
            ExamScreen(
                onNavigateBack = { navController.popBackStack() },
                onExamCreated = {
                    // This lambda is called when ExamScreen signals successful creation
                    println("DEBUG: AdminNavigation - onExamCreated called, navigating to ExamList.")
                    navController.navigate(AdminScreen.ExamList.route) {
                        // Pop AddExam from the back stack so pressing back from ExamList doesn't go to AddExam
                        popUpTo(AdminScreen.AddExam.route) { inclusive = true }
                        // Avoid multiple copies of ExamList if already there
                        launchSingleTop = true
                    }
                }
            )
        }

        // Define other admin screen routes from AdminScreen sealed class here
        composable(AdminScreen.Interview.route) {
            // Replace with your actual InterviewScreen composable
            // Text("Admin Interview Screen (Placeholder)")
        }
        composable(AdminScreen.Progress.route) {
            // Replace with your actual ProgressScreen composable
            // Text("Admin Progress Screen (Placeholder)")
        }
        composable(AdminScreen.Resources.route) {
            // Replace with your actual ResourcesScreen composable
            // Text("Admin Resources Screen (Placeholder)")
        }
    }
}