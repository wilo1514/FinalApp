package com.example.finalapp.data

import android.net.Uri
import com.example.finalapp.model.Response
import com.example.finalapp.repository.AddImageToStorageResponse
import com.example.finalapp.repository.AddImageUrlToFirestoreResponse
import com.example.finalapp.repository.GetImageFromFirestoreResponse
import com.example.finalapp.repository.ImageRepository
import com.example.finalapp.utils.Constants.IMAGES
import com.example.finalapp.utils.Constants.IMAGE_NAME
import com.example.finalapp.utils.Constants.UID
import com.example.finalapp.utils.Constants.URL
import com.example.finalapp.utils.Constants.CREATED_AT
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
): ImageRepository {
    override suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse {
        return try {
            val downloadUrl =storage.reference.child(IMAGES).child(IMAGE_NAME)
                .putFile(imageUri).await()
                .storage.downloadUrl.await()
            Response.Success(downloadUrl)
        }
        catch (e:Exception){
            Response.Failure(e)
        }
    }

    override suspend fun addImageUrlToFirestore(download: Uri): AddImageUrlToFirestoreResponse {
        return try {
            db.collection(IMAGES).document(UID).set(mapOf(
                URL to download,
                CREATED_AT to FieldValue.serverTimestamp()
            )).await()
            Response.Success(true)
        }catch (e:Exception){
            Response.Failure(e)
        }
    }

    override suspend fun getImageUrlfromFirestore(): GetImageFromFirestoreResponse {
        return try {
            val imageUrl = db.collection(IMAGES).document(UID).get().await().getString(URL)
            Response.Success(imageUrl)
        }catch (e:Exception){
            Response.Failure(e)
        }
    }
}