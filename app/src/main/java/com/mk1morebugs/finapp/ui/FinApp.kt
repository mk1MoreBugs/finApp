package com.mk1morebugs.finapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mk1morebugs.finapp.ui.components.FinappFloatingActionButton
import com.mk1morebugs.finapp.ui.components.FinappNavigationBar
import com.mk1morebugs.finapp.ui.components.FinappStatusBar
import com.mk1morebugs.finapp.ui.theme.FinappTheme


@Composable
fun Finapp() {

    FinappTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val coroutineScope = rememberCoroutineScope()

        val snackbarHostState = remember { SnackbarHostState()}

        val finappStatusbarTitle: MutableState<String> = remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                FinappStatusBar(
                    title = finappStatusbarTitle.value
                )
            },

            bottomBar = {
                FinappNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination,
                )
            },

            floatingActionButton = {
                if (currentDestination?.route == "home" || currentDestination?.route == "home/plan") {
                    FinappFloatingActionButton(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            },

            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }


        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                NavGraph(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    finappStatusbarTitle = finappStatusbarTitle
                )
            }
        }
    }
}