package com.example.ff.Login_Register_ForgotPass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.R
import com.example.ff.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtToDangNhap.setOnClickListener {
                var intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
        }
    }
}