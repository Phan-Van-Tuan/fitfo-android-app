package com.example.fitfo.Models
data class updateAvatarRequest(val avatar: String)
data class addFriendshipRequest(val user1: String,val user2: String)
data class updatePasswordRequest(val currentPassword: String,val newPassword: String)
data class createPostRequest(val author: String, val caption: String, val action:String, val photo: String)

class Request {
}