package com.aufarizazakipradana607062330127.asesment3.ui.screen
import com.aufarizazakipradana607062330127.asesment3.network.ApiStatus

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aufarizazakipradana607062330127.asesment3.model.KelolaProduk
import com.aufarizazakipradana607062330127.asesment3.network.KelolaProdukApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val _produkList = mutableStateOf<List<KelolaProduk>>(emptyList())
    val produkList: State<List<KelolaProduk>> = _produkList

    var data = mutableStateOf(emptyList<KelolaProduk>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = KelolaProdukApi.service.getKelolaProduk()
                val produk = response.data ?: emptyList()
                _produkList.value = produk

                data.value = produk
                _produkList.value = produk

                Log.d("MainViewModel", "Success: $produk")
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failure: ${e.message}", e)
            }
        }
    }
}