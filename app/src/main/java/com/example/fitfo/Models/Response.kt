package com.example.fitfo.Models
data class FriendShipResponse(val _id: String, val user1: String, val user2: String, val accepted: String )
data class ListFriendResponse(val chatId: String, val name: String, val avatar: String, val userId: String)
data class OutData(val latestSenderId: String,val chatId : String,val latestMessage :String,val latestType : String,val isRead : Boolean,val latestSend: String)
data class ChatResponse (
    val _id:String,
    val isRead: Boolean,
    val chatAvatar: String,
    val chatName: String,
    val members: List<String>,
    val latestSend: String,
    val latestType:String,
    val latestMessage:String,
    val latestSenderId:String)
class Response {
}