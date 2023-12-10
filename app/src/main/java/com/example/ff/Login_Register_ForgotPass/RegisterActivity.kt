package com.example.ff.Login_Register_ForgotPass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ff.Interface.ApiService
import com.example.ff.Models.RegisterRequest
import com.example.ff.Models.RegisterResponse
import com.example.ff.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtToLogin.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val phoneNumber = binding.edtPhoneNumber.text.toString()
            val password = binding.edtPassword.text.toString()
            val email = binding.edtEmail.text.toString()
            val name = binding.edtName.text.toString()
            val context: Context = this
            val RegisterRequest = RegisterRequest(name, phoneNumber, email, password)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://fitfo-api.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val call = apiService.registerUser(RegisterRequest)

            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        // Xử lý phản hồi thành công
                        val message = response.body()?.success ?: ""
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        // TODO: Xử lý message
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                        intent.putExtra("textView", message)
                        startActivity(intent);

                    } else {
                        // Xử lý phản hồi không thành công
                        // TODO: Xử lý lỗi

                        Toast.makeText(context, "phản hồi không thành công", Toast.LENGTH_SHORT)
                            .show();
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    // Xử lý lỗi khi request không thành công
                    // TODO: Xử lý lỗi
                    Toast.makeText(context, "lỗi khi request không thành công", Toast.LENGTH_SHORT)
                        .show();
                }
            })
        }
    }
}

