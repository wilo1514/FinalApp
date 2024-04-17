package com.example.finalapp.screens.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginScreenViewModel:ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun singInWhitEmailAndPassword(email: String, password:String, home: ()-> Unit)
            = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Log.d("Calculadora Publicitaria", "Loggin Succesfull")
                        home()
                    }else{
                        Log.d("Calculadora Publicitaria", "Loggin : ${task.result.toString()}")
                    }
                }
        }catch (e: Exception){
            Log.d("Calculadora Publicitaria", "Loggin: ${e.message}")
        }
    }
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        if (_loading.value == false ){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        creteUser(displayName)
                        home()
                    }else{
                        Log.d("Calculadora Publicitaria", "createUserWithEmailAndPassword : ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun creteUser(
        displayName: String?
    ) {
        val userId = auth.currentUser?.uid
        val user = mutableMapOf<String, Any>()
        user["user_id"] = userId.toString()
        user["display_name"] = displayName.toString()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Calculadora Publicitaria", "Creado ${it.id}")
            }.addOnFailureListener{
                Log.d("Calculadora Publicitaria", "Error ${it}")
            }

    }
}