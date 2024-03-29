package com.example.fitfo.Authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Logged
import com.example.fitfo.Models.LoginResponse
import com.example.fitfo.Models.LoginRequest
import com.example.fitfo.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = this.getSharedPreferences("remember_password", Context.MODE_PRIVATE)
        var rememberIsChecked = sharedPreferences.getBoolean("REMEMBER_IS_CHECKED", false)
        binding.rememberPassword.setChecked(rememberIsChecked);
        if (rememberIsChecked) {
            var rememberPhoneNumber =
                sharedPreferences.getString("REMEMBER_PHONE_NUMBER", null).toString()
            var rememberPassword = sharedPreferences.getString("REMEMBER_PASSWORD", null).toString()
            binding.inputPhoneNumber.setText(rememberPhoneNumber)
            binding.inputPassword.setText(rememberPassword)
        }


        binding.btnLogin.setOnClickListener {
            val context: Context = this
            val phoneNumber = binding.inputPhoneNumber.text.toString().trim();
            val password = binding.inputPassword.text.toString().trim();
            if (phoneNumber == "" || password == "") {
                Toast.makeText(context, "Bạn hãy điền đủ thông tin!", Toast.LENGTH_LONG).show()

            } else {
                val apiService = RetrofitClient.apiService
                val loginRequest = LoginRequest(phoneNumber, password)
                val call = apiService.loginUser(loginRequest)
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            // Xử lý phản hồi thành công
                            val name = response.body()?.name ?: ""
                            val id = response.body()?.id ?: ""
                            val phoneNumber = response.body()?.phoneNumber ?: ""
                            val avatar = response.body()?.avatar ?: ""
                            Log.d("avatarUrl", avatar)
                            val accessToken = response.body()?.token ?: ""
                            setData(id, name, avatar, phoneNumber, accessToken)
                            if (binding.rememberPassword.isChecked) {
                                rememberPassword(phoneNumber, password, true);
                            } else {
                                rememberPassword("", "", false);
                            }
                            // TODO: Xử lý message
                            val intent = Intent(
                                this@LoginActivity,
                                Logged::class.java
                            )
                            startActivity(intent);
                            finish()
                        } else {
                            // TODO: Xử lý phản hồi không thành công
                            Toast.makeText(
                                context,
                                "Số điện thoại hoặc mật khẩu không đúng!",
                                Toast.LENGTH_LONG
                            ).show();
                            binding.inputPhoneNumber.setText("");
                            binding.inputPassword.setText("");
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // TODO: Xử lý lỗi khi request không thành công
                        Toast.makeText(context, "đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    }
                })
            }
        }
        binding.btnToRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnToForgotPassword.setOnClickListener {
            var intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setData(id: String, name: String, avatar: String, phoneNumber: String, accessToken: String) {
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("MY_ID", id)
        editor.putString("MY_NAME", name)
        editor.putString("MY_PHONE_NUMBER", phoneNumber)
        editor.putString("MY_AVATAR", avatar)
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.apply()
        //day idol
    }

    private fun rememberPassword(phoneNumber: String, password: String, isChecked: Boolean) {
        sharedPreferences = this.getSharedPreferences("remember_password", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("REMEMBER_PASSWORD", password)
        editor.putString("REMEMBER_PHONE_NUMBER", phoneNumber)
        editor.putBoolean("REMEMBER_IS_CHECKED", isChecked)
        editor.apply()
    }

//    private fun takeData(){
//        sharedPreferences =this.getSharedPreferences("data", Context.MODE_PRIVATE)
//        val test= sharedPreferences.getString("ACCESS_TOKEN",null)
//    }
}