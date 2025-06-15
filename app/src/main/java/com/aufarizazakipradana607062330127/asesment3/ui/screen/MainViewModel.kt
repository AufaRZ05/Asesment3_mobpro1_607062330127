package com.aufarizazakipradana607062330127.asesment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aufarizazakipradana607062330127.asesment3.model.KelolaProduk
import com.aufarizazakipradana607062330127.asesment3.model.User
import com.aufarizazakipradana607062330127.asesment3.network.ApiStatus
import com.aufarizazakipradana607062330127.asesment3.network.KelolaProdukApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream


class MainViewModel : ViewModel() {

    private val _produkList = mutableStateOf<List<KelolaProduk>>(emptyList())

    var data = mutableStateOf(emptyList<KelolaProduk>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = KelolaProdukApi.service.getKelolaProduk(userId)
                val produk = response.kelolaproduk ?: emptyList()
                _produkList.value = produk

                data.value = produk
                _produkList.value = produk

                Log.d("MainViewModel", "Success: $produk $userId")
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failure: ${e.message}", e)
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, brandName: String, price: Int, stock: Int, category: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = KelolaProdukApi.service.postKelolaProduk(
                    userId = userId.toRequestBody("text/plain".toMediaType()),
                    brandName = brandName.toRequestBody("text/plain".toMediaType()),
                    price = price.toString().toRequestBody("text/plain".toMediaType()),
                    stock = stock.toString().toRequestBody("text/plain".toMediaType()),
                    category = category.toRequestBody("text/plain".toMediaType()),
                    image = bitmap.toMultipartBody()
                )

                if (result.status == "200")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(Id: Int, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                KelolaProdukApi.service.deleteKelolaProduk(Id)
                data.value = data.value.filter { it.id != Id }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }

}