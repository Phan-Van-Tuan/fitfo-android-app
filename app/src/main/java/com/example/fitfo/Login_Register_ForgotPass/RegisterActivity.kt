package com.example.fitfo.Login_Register_ForgotPass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.fitfo.Interface.ApiService
import com.example.fitfo.Models.RegisterRequest
import com.example.fitfo.Models.RegisterResponse
import com.example.fitfo.Test.Validator
import com.example.fitfo.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var validator = Validator();
    private var validate: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnToLogin.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.inputPhoneNumber.addTextChangedListener {
            var validatePhoneNumber = binding.inputPhoneNumber.text.toString().trim();
            if (validatePhoneNumber != "") {
                if (!validator.isPhoneNumber(validatePhoneNumber)) {
                    binding.layoutInputPhoneNumber.helperText = "Số điện thoại không hợp lệ"
                    return@addTextChangedListener
                }
            }
            binding.layoutInputPhoneNumber.helperText = ""
        }

        binding.inputEmail.addTextChangedListener {
            var validateEmail = binding.inputEmail.text.toString().trim();
            if (validateEmail != "") {
                if (!validator.isEmail(validateEmail)) {
                    binding.layoutInputEmail.helperText = "Email không hợp lệ";
                    return@addTextChangedListener
                }
            }
            binding.layoutInputEmail.helperText = ""
        }
        binding.inputPassword.addTextChangedListener {
            var validatePassword = binding.inputPassword.text.toString().trim();
            if (validatePassword != "") {
                if (!validator.hasMinimumLength(validatePassword)) {
                    binding.layoutInputPassword.helperText = "Mật khẩu phải có ít nhất 8 kí tự"
                    return@addTextChangedListener
                }
                if (!validator.containsUppercaseLetter(validatePassword)) {
                    binding.layoutInputPassword.helperText = "Mật khẩu phải có chữ viết hoa"
                    return@addTextChangedListener
                }
                if (!validator.containsDigit(validatePassword)) {
                    binding.layoutInputPassword.helperText = "Mật khẩu phải có kí tự số"
                    return@addTextChangedListener
                }
                if (!validator.containsSpecialCharacter(validatePassword)) {
                    binding.layoutInputPassword.helperText = "Mật khẩu phải có kí tự đặc biệt"
                    return@addTextChangedListener
                }
            } else {
                binding.layoutInputPassword.helperText = ""
            }
        }
        binding.inputConfirmPassword.addTextChangedListener {
            var validateConfirmPassword = binding.inputConfirmPassword.text.toString().trim();
            var validatePassword = binding.inputPassword.text.toString().trim();
            if (validateConfirmPassword != "") {
                if (validateConfirmPassword != validatePassword) {
                    binding.layoutInputConfirmPassword.helperText = "Mật khẩu không khớp!"
                    return@addTextChangedListener
                }
            }
            binding.layoutInputConfirmPassword.helperText = ""
        }

        binding.btnRegister.isEnabled = false;
        binding.checkboxAgree.setOnCheckedChangeListener { _, isChecked ->
            binding.btnRegister.isEnabled = isChecked
        }


        binding.btnRegister.setOnClickListener {
            val phoneNumber = binding.inputPhoneNumber.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val ConfirmPassword = binding.inputConfirmPassword.text.toString().trim();
            val email = binding.inputEmail.text.toString().trim()
            val name = binding.inputName.text.toString().trim()
            if (!validator.isRequired(phoneNumber)) binding.layoutInputPhoneNumber.helperText =
                "Trường này là bắt buộc";
            if (!validator.isRequired(email)) binding.layoutInputEmail.helperText =
                "Trường này là bắt buộc";
            if (!validator.isRequired(name)) binding.layoutInputName.helperText =
                "Trường này là bắt buộc";
            if (!validator.isRequired(password)) binding.layoutInputPassword.helperText =
                "Trường này là bắt buộc";

            validate =
                validator.isPhoneNumber(phoneNumber) && validator.isEmail(email) && validator.isStrongPassword(
                    password
                ) && (password == ConfirmPassword)

            if (!validate) return@setOnClickListener


            val context: Context = this
            val RegisterRequest = RegisterRequest(name, phoneNumber, email, password)

            val retrofit = Retrofit.Builder().baseUrl("https://fitfo-api.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val apiService = retrofit.create(ApiService::class.java)

            val call = apiService.registerUser(RegisterRequest)

            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>, response: Response<RegisterResponse>
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

