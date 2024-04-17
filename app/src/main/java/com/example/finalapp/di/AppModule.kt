package com.example.finalapp.di

import com.example.finalapp.data.ImageRepositoryImpl
import com.example.finalapp.repository.ImageRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    fun providesFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideImageRepository(
        storage: FirebaseStorage,
        db: FirebaseFirestore
    ):ImageRepository = ImageRepositoryImpl(
        storage = storage,
        db = db
    )
}