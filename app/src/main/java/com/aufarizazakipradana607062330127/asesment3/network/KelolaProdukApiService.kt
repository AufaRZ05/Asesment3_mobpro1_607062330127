package com.aufarizazakipradana607062330127.asesment3.network

import com.aufarizazakipradana607062330127.asesment3.model.KelolaProdukResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://103.87.67.74:3006/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface KelolaProdukApiService {
    @GET("/kelolaproduks?userId=aufa@gmail.com")
    suspend fun getKelolaProduk(): KelolaProdukResponse
}

object KelolaProdukApi {
    val service: KelolaProdukApiService by lazy {
        retrofit.create(KelolaProdukApiService::class.java)
    }

    fun getKelolaProdukUrl(imageId: String): String {
        return "$BASE_URL$imageId.png"
    }
}

enum class ApiStatus { LOADING, SUCCESS }