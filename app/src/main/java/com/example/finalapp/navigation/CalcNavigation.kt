package com.example.finalapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalapp.screens.CalculadoraScreen
import com.example.finalapp.screens.home.Home
import com.example.finalapp.screens.image.ImageScreen
import com.example.finalapp.screens.login.LoginScreen
import com.example.finalapp.screens.productlist.ProductListScreen

@Composable
fun CalculadoraNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = CalcScreens.CalcInitScreen.name
    ) {
        composable(CalcScreens.CalcInitScreen.name){
            CalculadoraScreen(navController = navController)
        }
        composable(CalcScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable(CalcScreens.HomeScren.name){
            Home(navController = navController)
        }
        composable(CalcScreens.GalleryScreen.name){
            ImageScreen(navController = navController)
        }
        composable(CalcScreens.ProductListSc.name){
            ProductListScreen(navController = navController)
        }

    }
}