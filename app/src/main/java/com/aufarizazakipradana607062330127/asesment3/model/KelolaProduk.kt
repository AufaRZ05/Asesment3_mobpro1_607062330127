package com.aufarizazakipradana607062330127.asesment3.model

import com.squareup.moshi.Json

data class KelolaProduk(
    @Json(name = "id")
    val id: Int,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "brandName")
    val brandName: String, // Disesuaikan dari namaMerek

    @Json(name = "price")
    val price: Int, // Disesuaikan dari harga

    @Json(name = "stock")
    val stock: Int, // Disesuaikan dari stok

    @Json(name = "category")
    val category: String, // Disesuaikan dari kategori

    @Json(name = "imageUrl")
    val imageUrl: String
)


data class KelolaProdukResponse(
    // Ubah nama variabel dari 'data' menjadi 'kelolaproduk' dan hapus anotasi @Json
    val kelolaproduk: List<KelolaProduk>?, // Nama variabel disesuaikan dengan key JSON

    @Json(name = "status")
    val status: String,

    @Json(name = "message")
    val message: String?
)