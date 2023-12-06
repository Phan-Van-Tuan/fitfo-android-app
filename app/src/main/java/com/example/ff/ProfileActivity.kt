package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.databinding.ActivityProfileBinding



class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val name = intent.getStringExtra("txtNameChat")
        binding.txtName.setText(name)
        binding.btnToChat.setOnClickListener {
            var intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("txtNameChat",name)
            startActivity(intent)
        }
    }
}