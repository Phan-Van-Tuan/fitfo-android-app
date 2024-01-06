package com.example.fitfo.Models
data class getFriendShipResponse(val _id: String, val user1: String, val user2: String, val accepted: String )
data class getListFriendResponse(val chatId: String, val name: String, val avatar: String, val userId: String)
class Response {
}