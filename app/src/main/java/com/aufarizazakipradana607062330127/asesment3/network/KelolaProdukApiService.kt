package com.aufarizazakipradana607062330127.asesment3.network

import com.aufarizazakipradana607062330127.asesment3.model.KelolaProdukResponse
import com.aufarizazakipradana607062330127.asesment3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://103.87.67.74:3006/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface KelolaProdukApiService {
    @GET("/kelolaproduks")
    suspend fun getKelolaProduk(
        @Query("userId") userId: String
    ): KelolaProdukResponse

    @Multipart
    @POST("/kelolaproduk")
    suspend fun postKelolaProduk(
        @Part("userId") userId: RequestBody,
        @Part("brandName") brandName: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("/kelolaproduk/{Id}")
    suspend fun deleteKelolaProduk(
        @Path("Id") id: Int
    )

    @Multipart
    @PUT("/kelolaproduk/{id}")
    suspend fun updateKelolaProduk(
        @Path("id") id: String,
        @Part("userId") userId: RequestBody,
        @Part("brandName") brandName: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part?
    ): OpStatus
}

object KelolaProdukApi {
    val service: KelolaProdukApiService by lazy {
        retrofit.create(KelolaProdukApiService::class.java)
    }

}

enum class ApiStatus { LOADING, SUCCESS, FAILED }