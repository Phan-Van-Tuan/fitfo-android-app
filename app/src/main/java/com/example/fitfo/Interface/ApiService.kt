package com.example.fitfo.Interface

import com.example.fitfo.Models.ChatResponse
import com.example.fitfo.Models.CommentRequest
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.Models.RegisterResponse
import com.example.fitfo.Models.LoginResponse
import com.example.fitfo.Models.GetUserByPhoneNumberResponse
import com.example.fitfo.Models.LoginRequest
import com.example.fitfo.Models.RegisterRequest
import com.example.fitfo.Models.addFriendshipRequest
import com.example.fitfo.Models.createPostRequest
import com.example.fitfo.Models.findCommentResponse
import com.example.fitfo.Models.findMessageResponse
import com.example.fitfo.Models.FriendShipResponse
import com.example.fitfo.Models.ListFriendResponse
import com.example.fitfo.Models.updateAvatarRequest
import com.example.fitfo.Models.updatePasswordRequest
import com.example.fitfo.Models.updatePostLikeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @POST("api/user/login")
    fun loginUser(@Body requestBody: LoginRequest): Call<LoginResponse>
    @POST("api/user/register")
    fun registerUser(@Body requestBody: RegisterRequest): Call<RegisterResponse>
    @PUT("api/user/updatePassword/{myId}")
    fun updatePassword(@Path("myId") myId: String, @Body requestBody: updatePasswordRequest): Call<String>
    @GET("api/user/getUserByPhoneNumber/{phoneNumber}")
    fun getUserByPhoneNumber(@Path("phoneNumber") phoneNumber: String): Call<GetUserByPhoneNumberResponse>
    @GET("api/chat/find/{myId}/{userId}")
    fun findChat(@Path("myId") myId: String, @Path("userId") userId: String): Call<List<ChatResponse>>
    @GET("api/chat/{myId}")
    fun findChats(@Path("myId") myId: String): Call<List<ChatResponse>>
    @GET("api/message/{chatId}")
    fun findMessages(@Path("chatId") myId: String): Call<List<findMessageResponse>>
    @GET("api/friendship/{myId}")
    fun findContact(@Path("myId") myId: String): Call<List<ListFriendResponse>>
    @GET("api/post/getAll/{myId}")
    fun findPosts(@Path("myId") myId: String): Call<List<GetPostReponse>>
    @POST("api/post")
    fun createPost(@Body requestBody: createPostRequest): Call<String>
    @GET("api/post/getByUser/{userId}")
    fun findPostsOfUser(@Path("userId") userId: String): Call<List<GetPostReponse>>
    @PATCH("api/post/like/{postId}")
    fun updatePostLike(@Path("postId") postId: String, @Body requestBody: updatePostLikeRequest): Call<String>
    fun deletePost(@Path("id") id: String): Call<String>
    @GET("api/story")
    fun findStory(): Call<List<GetStoryReponse>>
    @GET("api/comment/{postId}")
    fun findComment(@Path("postId") postId: String): Call<List<findCommentResponse>>
    @POST("api/comment/{postId}")
    fun comment(@Path("postId") postId: String, @Body requestBody: CommentRequest): Call<String>
    @DELETE("api/comment/{commentId}")
    fun deleteComment(@Path("commentId") commentId: String): Call<String>
    @PATCH("/api/user/updateAvatar/{myId}")
    fun updateAvatar(@Path("myId") myId: String, @Body requestBody: updateAvatarRequest): Call<String>
    @GET("api/friendship/{myId}/{userId}")
    fun getFriendship(@Path("myId") myId: String, @Path("userId") userId: String): Call<FriendShipResponse>
    @POST("api/friendship/addFriend")
    fun addFriend(@Body requestBody: addFriendshipRequest): Call<String>
    @PATCH("api/friendship/acceptFriend/{friendshipId}")
    fun acceptFriend(@Path("friendshipId") friendshipId: String): Call<String>
    @DELETE("api/friendship/deleteFriend/{friendshipId}")
    fun deleteFriend(@Path("friendshipId") friendshipId: String): Call<String>
}