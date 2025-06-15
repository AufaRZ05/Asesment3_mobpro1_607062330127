package com.aufarizazakipradana607062330127.asesment3.model

import com.squareup.moshi.Json

data class KelolaProduk(
    @Json(name = "id")
    val id: Int,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "brandName")
    val brandName: String,

    @Json(name = "price")
    val price: Int,

    @Json(name = "stock")
    val stock: Int,

    @Json(name = "category")
    val category: String,

    @Json(name = "imageUrl")
    val imageUrl: String
)


data class KelolaProdukResponse(
    val kelolaproduk: List<KelolaProduk>?,

    @Json(name = "status")
    val status: String,

    @Json(name = "message")
    val message: String?
)