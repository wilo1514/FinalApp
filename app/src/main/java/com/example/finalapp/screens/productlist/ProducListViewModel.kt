package com.example.finalapp.screens.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductListViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    fun getProductList(
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("productos").get().await()
                val productList = mutableListOf<Map<String, Any>>()
                for (document in querySnapshot.documents) {
                    val data = document.data
                    data?.let {
                        productList.add(it)
                    }
                }
                onSuccess(productList)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}
