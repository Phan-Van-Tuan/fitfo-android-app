package com.example.ff.OptionActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.PersonalActivity
import com.example.ff.databinding.ActivityOptionPersonalBinding

class OptionPersonal : AppCompatActivity() {
    private lateinit var binding: ActivityOptionPersonalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBacktoPersonal.setOnClickListener {
            var intent = Intent(this, PersonalActivity::class.java)
            startActivity(intent)
        }
    }
}