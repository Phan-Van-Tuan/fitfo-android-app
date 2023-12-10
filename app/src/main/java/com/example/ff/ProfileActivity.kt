package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.Test.NameChat
import com.example.ff.databinding.ActivityProfileBinding



class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val userName = intent.getStringExtra("userName")
        binding.txtName.setText(NameChat.userName)
        binding.btnToChat.setOnClickListener {
            var intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
    }
}