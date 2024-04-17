
package com.example.finalapp.screens.image



import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalapp.navigation.CalcScreens


@Composable
fun ImageScreen(navController: NavController, viewModel: ImageUploadViewModel = viewModel()) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val nombre = remember { mutableStateOf("") }
    val units = remember { mutableStateOf("") }
    val cost = remember { mutableStateOf("") }

    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(CalcScreens.ProductListSc.name) }) {
            Text("Ir a lista de productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Producto")
        InputField(valueState = nombre.value, onValueChange = { nombre.value = it }, label = "Producto")

        Spacer(modifier = Modifier.height(8.dp))
        Text("Unidades")
        InputField(valueState = units.value, onValueChange = { units.value = it }, label = "Unidades")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Costo")
        InputField(valueState = cost.value, onValueChange = { cost.value = it }, label = "Costo")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { getContent.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri.value?.let { uri ->
            UploadButton(
                uri = uri,
                nom = nombre.value,
                units = units.value,
                cost = cost.value.toFloatOrNull() ?: 0f,
                context = context,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun UploadButton(
    uri: Uri,
    nom: String,
    units: String,
    cost: Float,
    context: Context,
    navController: NavController,
    viewModel: ImageUploadViewModel
) {
    Button(onClick = {
        viewModel.uploadImageAndStoreData(
            uri = uri,
            nom = nom,
            units = units,
            cost = cost,
            context = context,
            onSuccess = {

                Toast.makeText(context, "Carga exitosa", Toast.LENGTH_SHORT).show()
            },
            onError = {
                // Manejar error si es necesario
            }
        )
    }) {
        Text("Subir imagen y datos")
    }
}

@Composable
fun InputField(
    valueState: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valueState,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    )
}