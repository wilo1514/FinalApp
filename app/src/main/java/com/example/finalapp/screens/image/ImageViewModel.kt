package com.example.finalapp.screens.image


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ImageUploadViewModel : ViewModel() {

    fun uploadImageAndStoreData(
        uri: Uri,
        nom : String,
        units: String,
        cost: Float,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = uploadImageAndGetUrl(uri)
                imageUrl?.let { url ->
                    storeProductInFirestore(url,nom, units, cost)
                    onSuccess("Imagen y datos subidos exitosamente")
                } ?: onError("Error al subir imagen")
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    private suspend fun uploadImageAndGetUrl(uri: Uri): String? {
        val storageRef = Firebase.storage.reference.child("images/${System.currentTimeMillis()}")
        val uploadTask = storageRef.putFile(uri)
        val taskSnapshot = uploadTask.await()
        return taskSnapshot.storage.downloadUrl.await().toString()
    }

    private suspend fun storeProductInFirestore(imageUrl: String, nom: String,units: String, cost: Float) {
        val db = Firebase.firestore
        val data = hashMapOf(
            "imageUrl" to imageUrl,
            "nombre" to nom,
            "units" to units,
            "cost" to cost
        )
        db.collection("productos").add(data).await()
    }
}
