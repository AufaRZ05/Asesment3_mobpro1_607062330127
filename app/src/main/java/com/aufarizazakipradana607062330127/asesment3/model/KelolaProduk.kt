package com.aufarizazakipradana607062330127.asesment3.model

import com.squareup.moshi.Json

data class KelolaProduk(
    @Json(name = "id")
    val id: Int,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "brandName")
    val namaMerek: String,

    @Json(name = "price")
    val harga: Int,

    @Json(name = "stock")
    val stok: Int,

    @Json(name = "category")
    val kategori: String,

    @Json(name = "imageUrl")
    val imageUrl: String
)


data class KelolaProdukResponse(
    @Json(name = "kelolaproduk")
    val data: List<KelolaProduk>?,

    @Json(name = "status")
    val status: Int
)
