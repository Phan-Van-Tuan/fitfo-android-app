package com.example.ff.Login_Register_ForgotPass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.ff.Interface.ApiService
import com.example.ff.Models.RegisterRequest
import com.example.ff.Models.RegisterResponse
import com.example.ff.Test.Validator
import com.example.ff.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private var validator = Validator();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnToLogin.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.inputPhoneNumber.addTextChangedListener{
            var validatePhoneNumber = binding.inputPhoneNumber.text.toString().trim();
            if (validatePhoneNumber != "") {
                binding.layoutInputPhoneNumber.helperText = validator.isPhoneNumber(validatePhoneNumber);
            }else{
                binding.layoutInputPhoneNumber.helperText = ""
            }
        }
        binding.inputEmail.addTextChangedListener{
            var validateEmail = binding.inputEmail.text.toString().trim();
            if (validateEmail != "") {
                binding.layoutInputEmail.helperText = validator.isEmail(validateEmail);
            }else{
                binding.layoutInputEmail.helperText = ""
            }
        }
        binding.inputPassword.addTextChangedListener{
            var validatePassword = binding.inputPassword.text.toString().trim();
            if (validatePassword != "") {
                binding.layoutInputPassword.helperText = validator.isStrongPassword(validatePassword);
            }else{
                binding.layoutInputPassword.helperText = ""
            }
        }
        binding.inputConfirmPassword.addTextChangedListener{
            var validateConfirmPassword = binding.inputConfirmPassword.text.toString().trim();
            var validatePassword = binding.inputPassword.text.toString().trim();
            if (validateConfirmPassword != "") {
                if (validatePassword != ""){
                    if(validateConfirmPassword != validatePassword)
                        binding.layoutInputConfirmPassword.helperText = "Mật khẩu không khớp!"
                }else{
                    binding.layoutInputConfirmPassword.helperText = "Hãy nhập mật khẩu!"
                }

            }else{
                binding.layoutInputConfirmPassword.helperText = ""
            }
        }

        binding.btnRegister.setOnClickListener {
            val phoneNumber = binding.inputPhoneNumber.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val name = binding.inputName.text.toString().trim()
            if(!validator.isRequired(phoneNumber))
                binding.layoutInputPhoneNumber.helperText = "Trường này là bắt buộc";
            if(!validator.isRequired(email))
                binding.layoutInputEmail.helperText = "Trường này là bắt buộc";
            if(!validator.isRequired(name))
                binding.layoutInputName.helperText = "Trường này là bắt buộc";
            if(!validator.isRequired(password))
                binding.layoutInputPassword.helperText = "Trường này là bắt buộc";



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
                        val intent = Intent(context, LoginActivity::class.java)
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

