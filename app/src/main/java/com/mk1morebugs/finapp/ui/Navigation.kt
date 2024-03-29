package com.mk1morebugs.finapp.ui


import androidx.navigation.NavHostController



sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("home/history")
    object Plan : Screen("home/plan")

    object AddData : Screen("home/add-data")
    object AddCategory : Screen("home/add-data/add-category")

    object AddPlan : Screen("home/plan/add-plan")
}

class Navigation(navController: NavHostController) {



    val navigateToAddData: () -> Unit = {
        navController.navigate(Screen.AddData.route) {
            popUpTo(Screen.Home.route)

        }
    }


    val navigateToAddPlan: () -> Unit = {
        navController.navigate(Screen.AddPlan.route) {

            popUpTo(Screen.Plan.route)
        }
    }

    val navigateToAddCategory: () -> Unit = {
        navController.navigate(Screen.AddCategory.route) {

            popUpTo(Screen.AddCategory.route)
        }
    }

    /*fun getNavigate(route: String, navController: NavHostController) {

        when (route) {
            Screen.Home.route ->
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true

            }
            Screen.Plan.route ->
                navController.navigate(Screen.Plan.route) {
                    popUpTo(Screen.Plan.route) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true

                }
            Screen.History.route ->
                navController.navigate(Screen.History.route) {
                    popUpTo(Screen.History.route) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true

                }
        }
    }*/


}
