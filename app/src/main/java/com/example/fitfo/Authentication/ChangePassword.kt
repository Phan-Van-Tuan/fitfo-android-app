package com.example.fitfo.Authentication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.fitfo.Profile.Account_Security
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.Dialog
import com.example.fitfo.Define.Validator
import com.example.fitfo.Models.updatePasswordRequest
import com.example.fitfo.databinding.ActivityChangePasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private var validator = Validator();
    private var confirmNewPassword: String = ""
    private var newPassword: String = ""
    private var oldPassword: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString();

        binding.btnChangePassword.setOnClickListener {
            updatePassword(myId, oldPassword ,newPassword)
        }


        binding.confirmNewPassword.addTextChangedListener {
            confirmNewPassword = binding.confirmNewPassword.text.toString().trim()
            newPassword = binding.newPassword.text.toString().trim()
            if (newPassword == confirmNewPassword || confirmNewPassword == "") {
                binding.layoutConfirmNewPassword.helperText = ""
                return@addTextChangedListener
            } else {
                binding.layoutConfirmNewPassword.helperText = "Mật khẩu mới không khớp"
                return@addTextChangedListener
            }
        }
        binding.newPassword.addTextChangedListener {
            newPassword = binding.newPassword.text.toString().trim()
            oldPassword = binding.oldPassword.text.toString().trim()
            var validatePassword = binding.newPassword.text.toString().trim();
            if (validatePassword != "") {
                if (!validator.hasMinimumLength(validatePassword)) {
                    binding.layoutNewInputPassword.helperText = "Mật khẩu phải có ít nhất 8 kí tự"
                    return@addTextChangedListener
                }
                if (!validator.containsUppercaseLetter(validatePassword)) {
                    binding.layoutNewInputPassword.helperText = "Mật khẩu phải có chữ viết hoa"
                    return@addTextChangedListener
                }
                if (!validator.containsDigit(validatePassword)) {
                    binding.layoutNewInputPassword.helperText = "Mật khẩu phải có kí tự số"
                    return@addTextChangedListener
                }
                if (!validator.containsSpecialCharacter(validatePassword)) {
                    binding.layoutNewInputPassword.helperText = "Mật khẩu phải có kí tự đặc biệt"
                    return@addTextChangedListener
                }
                if(newPassword == oldPassword) {
                    binding.layoutNewInputPassword.helperText = "Mật khẩu không được trùng mật khẩu cữ"
                    return@addTextChangedListener
                }
                binding.layoutNewInputPassword.helperText = ""
            } else {
                binding.layoutNewInputPassword.helperText = ""
            }
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Account_Security::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun updatePassword( myId: String,currentPassword: String, newPassword: String) {
        val apiService = RetrofitClient.apiService
        val updatePasswordRequest = updatePasswordRequest(currentPassword, newPassword)
        val call = apiService.updatePassword(myId, updatePasswordRequest)
        val dialog = Dialog()
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    dialog.showMessageDialog(
                        this@ChangePassword,
                        "Success",
                        "bạn vừa đổi mật khẩu thành công, chuyển hướng đến màn hình đăng nhập"
                    )
                    val intent = Intent(this@ChangePassword, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    dialog.showMessageDialog(
                        this@ChangePassword,
                        "Error",
                        "bạn vừa đổi mật khẩu thất bại!"
                    )
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                dialog.showMessageDialog(
                    this@ChangePassword,
                    "Error",
                    "Server hỏng rồi!"
                )
            }
        })
    }
}