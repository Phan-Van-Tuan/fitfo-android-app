package com.example.ff.Login_Register_ForgotPass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ff.Interface.ApiService
import com.example.ff.MainActivity_Logged_in
import com.example.ff.Models.LoginResponse
import com.example.ff.Models.LoginRequest
import com.example.ff.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

                val loginRequest = LoginRequest(phoneNumber, password)

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://fitfo-api.vercel.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)

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
                            val accessToken = response.body()?.token ?: ""
                            setData(id, name, phoneNumber, accessToken)
                            if (binding.rememberPassword.isChecked) {
                                rememberPassword(phoneNumber, password, true);
                            } else {
                                rememberPassword("", "", false);
                            }
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            // TODO: Xử lý message
                            val intent =
                                Intent(this@LoginActivity, MainActivity_Logged_in::class.java)
//                        intent.putExtra("textView", message)
                            startActivity(intent);
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
        }
        binding.btnToForgotPassword.setOnClickListener {
            var intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setData(id: String, name: String, phoneNumber: String, accessToken: String) {
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("MY_ID", id)
        editor.putString("MY_NAME", name)
        editor.putString("MY_PHONE_NUMBER", phoneNumber)
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.apply()
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