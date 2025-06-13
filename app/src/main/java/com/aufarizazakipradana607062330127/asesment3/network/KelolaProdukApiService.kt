package com.aufarizazakipradana607062330127.asesment3.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://103.87.67.74:3006/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface KelolaProdukApiService {
    @GET("/kelolaproduks?userId=aufa@gmail.com")
    suspend fun getKelolaProduk(): String
}

object KelolaProdukApi {
    val service: KelolaProdukApiService by lazy {
        retrofit.create(KelolaProdukApiService::class.java)
    }
}