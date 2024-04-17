package com.example.finalapp.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun Home(navController: NavController) {
    /*LaunchedEffect(key1 = true) {
        delay(3500L)
        navController.navigate(CalculadoraScreens.LoginScreen.name)

    }*/
    Text(text = "Home")
}