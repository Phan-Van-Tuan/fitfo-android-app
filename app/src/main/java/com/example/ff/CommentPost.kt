package com.example.ff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ff.Adapter.CommentAdapter
import com.example.ff.Adapter.ListMessageAdapter
import com.example.ff.Interface.ApiService
import com.example.ff.Models.GetStoryReponse
import com.example.ff.Models.findCommentResponse
import com.example.ff.Models.findMessageResponse
import com.example.ff.databinding.ActivityCommentPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentPost : AppCompatActivity() {
    private lateinit var binding: ActivityCommentPostBinding
    private var listComments: MutableList<findCommentResponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}