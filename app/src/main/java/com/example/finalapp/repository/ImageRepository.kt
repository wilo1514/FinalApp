package com.example.finalapp.repository


import android.net.Uri
import com.example.finalapp.model.Response

typealias AddImageToStorageResponse = Response<Uri>
typealias AddImageUrlToFirestoreResponse = Response<Boolean>
typealias GetImageFromFirestoreResponse = Response<String>


interface ImageRepository {
    suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse
    suspend fun addImageUrlToFirestore(download: Uri): AddImageUrlToFirestoreResponse
    suspend fun getImageUrlfromFirestore(): GetImageFromFirestoreResponse
}