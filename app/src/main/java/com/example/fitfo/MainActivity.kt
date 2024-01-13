package com.example.fitfo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.example.fitfo.Authentication.LoginActivity
import com.example.fitfo.Authentication.RegisterActivity
import com.example.fitfo.Define.FullScreenBottomSheetDialog
import com.example.fitfo.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences =this.getSharedPreferences("data",Context.MODE_PRIVATE)
        val id: String= sharedPreferences.getString("MY_ID","").toString()
        if (id==""){
            }else{
            var intent = Intent(this,Logged::class.java)
            startActivity(intent)
        }
        binding.btnToRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnToLogin.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }


}