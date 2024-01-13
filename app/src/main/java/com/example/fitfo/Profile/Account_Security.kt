package com.example.fitfo.Profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitfo.Authentication.ChangePassword
import com.example.fitfo.Logged
import com.example.fitfo.databinding.ActivityAccountSecurityBinding

class Account_Security : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSecurityBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backtopeople.setOnClickListener {
            onBackPressed()
        }
        sharedPreferences =this.getSharedPreferences("data", Context.MODE_PRIVATE)
        binding.phoneNumber.setText(sharedPreferences.getString("MY_PHONE_NUMBER",null).toString())

        binding.textView11.setOnClickListener {
            var intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }


    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Logged::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}