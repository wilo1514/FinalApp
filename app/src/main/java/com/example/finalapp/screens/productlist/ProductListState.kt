
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductListViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _productListState: MutableStateFlow<List<Map<String, Any>>> = MutableStateFlow(emptyList())
    val productListState: StateFlow<List<Map<String, Any>>> = _productListState

    fun getProductList() {
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
                _productListState.value = productList
            } catch (e: Exception) {
                // Manejar errores aquí si es necesario
            }
        }
    }
}
