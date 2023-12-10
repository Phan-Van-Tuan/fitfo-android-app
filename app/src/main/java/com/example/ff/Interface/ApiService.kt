package com.example.ff.Interface

import com.example.ff.Models.RegisterResponse
import com.example.ff.Models.LoginResponse
import com.example.ff.Models.GetUserByPhoneNumberResponse
import com.example.ff.Models.LoginRequest
import com.example.ff.Models.RegisterRequest
import com.example.ff.Models.findChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @POST("api/users/login")
    fun loginUser(@Body requestBody: LoginRequest): Call<LoginResponse>
    @POST("api/users/register")
    fun registerUser(@Body requestBody: RegisterRequest): Call<RegisterResponse>
    @GET("api/users/getUserByPhoneNumber/{phoneNumber}")
    fun getUserByPhoneNumber(@Path("phoneNumber") phoneNumber: String): Call<GetUserByPhoneNumberResponse>
    @GET("api/chats/find/{myId}/{userId}")
    fun findChat(@Path("myId") myId: String, @Path("userId") userId: String): Call<List<findChatResponse>>

}