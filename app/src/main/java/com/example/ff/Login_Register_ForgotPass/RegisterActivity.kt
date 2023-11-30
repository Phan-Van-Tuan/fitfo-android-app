package com.example.ff.Login_Register_ForgotPass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ff.databinding.ActivityRegisterBinding
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.PhoneNumberValidityChangeListener


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
        binding.ccp.registerCarrierNumberEditText(binding.edtPhoneNumber)


    }
}