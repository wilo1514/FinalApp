package com.example.finalapp.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.finalapp.navigation.CalcScreens
import kotlinx.coroutines.delay


@Composable
fun CalculadoraScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(3500L)
        navController.navigate(CalcScreens.LoginScreen.name)

    }
    Text(text = "Pantalla inicial")
}