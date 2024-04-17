package com.example.finalapp.model


data class Product(
    val nombre: String,
    val units: String,
    val cost: Float,
    val imageUrl: String // La URL de la imagen en Firebase Storage
)