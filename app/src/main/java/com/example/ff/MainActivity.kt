package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.MainActivity_Logged_in
import com.example.ff.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txbam.setOnClickListener {
            var intent = Intent(this, MainActivity_Logged_in::class.java)
            startActivity( intent)
        }
    }

}