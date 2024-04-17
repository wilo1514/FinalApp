package com.example.finalapp.screens.productlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finalapp.navigation.CalcScreens
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ProductListScreen(navController)
        }
    }
}

@Composable
fun ProductListScreen(navController: NavController, viewModel: ProductListViewModel = viewModel()) {
    var productListState by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val context = LocalContext.current

    val refreshButtonState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getProductList(
            onSuccess = { productList ->
                productListState = productList
            },
            onFailure = { exception ->
                Log.e("ProductListScreen", "Error fetching product list", exception)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Lista de Productos")
        Button(onClick = { navController.navigate(CalcScreens.GalleryScreen.name) }) {
            Text("Ir a ImageScreen")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { refreshButtonState.value = true }) {
            Text("Actualizar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProductListView(productList = productListState)

    }

    if (refreshButtonState.value) {
        LaunchedEffect(Unit) {
            viewModel.getProductList(
                onSuccess = { productList ->
                    productListState = productList
                    refreshButtonState.value = false
                },
                onFailure = { exception ->
                    Log.e("ProductListScreen", "Error fetching product list", exception)
                    refreshButtonState.value = false
                }
            )
        }
    }
}

@Composable
fun ProductListView(productList: List<Map<String, Any>>) {
    LazyColumn {
        items(productList) { product ->
            ProductItemView(product)
        }
    }
}

@Composable
fun ProductItemView(product: Map<String, Any>) {
    val imageUrl = product["imageUrl"] as? String
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUrl) {
        if (!imageUrl.isNullOrEmpty()) {
            loadImageFromStorage(imageUrl)?.let { loadedBitmap ->
                bitmap = loadedBitmap
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        bitmap?.let { loadedBitmap ->
            Image(
                bitmap = loadedBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
        Text(text = "Nombre: ${product["nombre"]}")
        Text(text = "Unidades: ${product["units"]}")
        Text(text = "Costo: ${product["cost"]}")
    }
}

suspend fun loadImageFromStorage(imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val file = File.createTempFile("tempImage", "jpg")
            Firebase.storage.getReferenceFromUrl(imageUrl).getFile(file).await()
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            Log.e("ProductListScreen", "Error loading image from Storage: $imageUrl", e)
            null
        }
    }
}

@Preview
@Composable
fun PreviewProductListScreen() {
    ProductListScreen(rememberNavController())
}
