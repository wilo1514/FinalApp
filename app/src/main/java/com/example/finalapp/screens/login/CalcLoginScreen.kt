package com.example.finalapp.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalapp.navigation.CalcScreens


@Composable
fun LoginScreen(navController: NavController,
                viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val showLoginForm =  rememberSaveable {
        mutableStateOf(true)
    }
    val context =  LocalContext.current
    Surface (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showLoginForm.value){
                Text(text = "Login Session")
                UserForm(
                    isCreateAccount = false
                ){
                        email, password->
                    Log.d("Calculadora Publicitaria", "$email is Login Successful y $password")
                    viewModel.singInWhitEmailAndPassword(email, password){
                        Toast
                            .makeText(
                                context,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                            navController.navigate(CalcScreens.ProductListSc.name)
                    }
                }
            }else{
                Text(text = "Create Account")
                UserForm(
                    isCreateAccount = true
                ){
                        email, password->
                    Log.d("Calculadora Publicitaria", "$email Create Account Successful ")
                    viewModel.createUserWithEmailAndPassword(email,password){
                        Toast
                            .makeText(
                                context,
                                "Create User is Successful",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }

                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                val text1 =
                    if (showLoginForm.value) "¿No tienes cuenta?"
                    else "¿Ya tienes cuenta?"
                val text2 =
                    if (showLoginForm.value)
                        "Registrate"
                    else
                        "Inicia Sesión"
                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.primary
                )


            }
        }


    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String,String)-> Unit = {email, pwd ->}
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valid = remember (email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        EmailInput(
            emailState = email
        )
        PaswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Login",
            inputValid = valid
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValid: Boolean,
    onClick: ()->Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValid
    ) {
        Text(text = textId,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@Composable
fun PaswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()){
                PasswordvisibleIcon(passwordVisible)
            }
            else null

        }
    )
}

@Composable
fun PasswordvisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }

}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId : String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType =KeyboardType.Email

    )
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = { Text(text = labelId)},
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}
