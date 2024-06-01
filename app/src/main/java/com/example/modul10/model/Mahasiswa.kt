package com.example.modul10.model

import com.google.gson.annotations.SerializedName

class Mahasiswa {
    @SerializedName("nama")
    var nama: String? = null
    @SerializedName("jurusan")
    var jurusan: String? = null
    @SerializedName("id")
    val id: String? = null
    @SerializedName("nrp")
    var nrp: String? = null
    @SerializedName("email")
    var email: String? = null
}