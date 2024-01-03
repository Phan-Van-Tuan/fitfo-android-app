package com.example.fitfo.Interface

import com.example.fitfo.Models.CommentRequest
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.Models.RegisterResponse
import com.example.fitfo.Models.LoginResponse
import com.example.fitfo.Models.GetUserByPhoneNumberResponse
import com.example.fitfo.Models.LoginRequest
import com.example.fitfo.Models.RegisterRequest
import com.example.fitfo.Models.findChatResponse
import com.example.fitfo.Models.findCommentResponse
import com.example.fitfo.Models.findMessageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @POST("api/user/login")
    fun loginUser(@Body requestBody: LoginRequest): Call<LoginResponse>
    @POST("api/user/register")
    fun registerUser(@Body requestBody: RegisterRequest): Call<RegisterResponse>
    @GET("api/user/getUserByPhoneNumber/{phoneNumber}")
    fun getUserByPhoneNumber(@Path("phoneNumber") phoneNumber: String): Call<GetUserByPhoneNumberResponse>
    @GET("api/chat/find/{myId}/{userId}")
    fun findChat(@Path("myId") myId: String, @Path("userId") userId: String): Call<List<findChatResponse>>
    @GET("api/chat/{myId}")
    fun findChats(@Path("myId") myId: String): Call<List<findChatResponse>>
    @GET("api/message/{chatId}")
    fun findMessages(@Path("chatId") myId: String): Call<List<findMessageResponse>>
    @GET("api/post/getAll")
    fun findPosts(): Call<List<GetPostReponse>>
    @DELETE("api/post/{id}")
    fun deletePost(@Path("id") id: String): Call<String>
    @GET("api/story")
    fun findStory(): Call<List<GetStoryReponse>>
    @GET("api/comment/{postId}")
    fun findComment(@Path("postId") postId: String): Call<List<findCommentResponse>>

    @POST("api/comment/{postId}")
    fun comment(@Path("postId") postId: String, @Body requestBody: CommentRequest): Call<String>

    @DELETE("api/comment/{id}")
    fun deleteComment(@Path("commentId") id: String): Call<String>
}