package com.example.ff.Interface

import com.example.ff.Models.ApiRegister
import com.example.ff.Models.ApiLogin
import com.example.ff.Models.LoginRequest
import com.example.ff.Models.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("api/users/login")
    fun loginUser(@Body requestBody: LoginRequest): Call<ApiLogin>
    @POST("api/users/register")
    fun registerUser(@Body requestBody: RegisterRequest): Call<ApiRegister>
}