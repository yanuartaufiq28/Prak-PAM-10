package com.example.modul10.api

import com.example.modul10.model.AddMahasiswaResponse
import com.example.modul10.model.MahasiswaResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("mahasiswa")
    fun getMahasiswa(@Query("nrp") nrp: String): Call<MahasiswaResponse>

    @POST("mahasiswa")
    @FormUrlEncoded
    fun addMahasiswa(
        @Field("nrp") nrp: String,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("jurusan") jurusan: String
    ): Call<AddMahasiswaResponse>

    @GET("mahasiswa")
    fun getAllMahasiswa(): Call<MahasiswaResponse>

    @DELETE("mahasiswa/{id}")
    fun deleteMahasiswa(
        @Path("id") id: String
    ): Call<Void>

    @PUT("mahasiswa/{id}")
    @FormUrlEncoded
    fun updateMahasiswa(
        @Path("id") id: String,
        @Field("nrp") nrp: String,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("jurusan") jurusan: String
    ): Call<Void>

}