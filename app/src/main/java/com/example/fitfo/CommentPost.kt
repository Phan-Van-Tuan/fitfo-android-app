package com.example.fitfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitfo.Models.findCommentResponse
import com.example.fitfo.databinding.ActivityCommentPostBinding

class CommentPost : AppCompatActivity() {
    private lateinit var binding: ActivityCommentPostBinding
    private var listComments: MutableList<findCommentResponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}