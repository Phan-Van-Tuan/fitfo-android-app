package com.example.fitfo.Define.CallApi

import com.example.fitfo.Interface.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl("http://192.168.2.9:3200/")
        .baseUrl("http://192.168.1.24:3200/")
//        .baseUrl("https://fitfo-api.vercel.app/")
//        .baseUrl("http://192.168.179.161:3200/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}