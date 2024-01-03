package com.example.ff.Models

data class GetPostReponse(val _id:String, val author: String, val userName: String, val avatar: String, val caption: String, val action: String, val like: List<String>, val photo: String)
