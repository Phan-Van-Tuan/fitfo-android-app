package com.example.fitfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Models.GetUserByPhoneNumberResponse
import com.example.fitfo.Profile.ProfileActivity
import com.example.fitfo.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val phoneNumberInsert = newText?.trim()

                if (phoneNumberInsert != null) {
                    if (phoneNumberInsert.length==10){
                        binding.frameSearch.visibility = View.VISIBLE
                        val apiService = RetrofitClient.apiService
                        val phoneNumber = newText ?: ""

                        val call = apiService.getUserByPhoneNumber(phoneNumber)
                        call.enqueue(object : Callback<GetUserByPhoneNumberResponse> {
                            override fun onResponse(
                                call: Call<GetUserByPhoneNumberResponse>,
                                response: Response<GetUserByPhoneNumberResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val userResponse = response.body()

                                    if (userResponse != null) {
                                        val userId = userResponse.id
                                        val userName = userResponse.name
                                        val userAvatar = userResponse.avatar
                                        val userPhoneNumber = userResponse.phoneNumber
                                        UserInfo.userName= userName
                                        UserInfo.userID= userId
                                        UserInfo.userAvatar = userAvatar
                                        binding.textName.setText(userName)
                                        if (!userAvatar.isNullOrEmpty() ) {
                                            ImageUtils.displayImage2(userAvatar, binding.imgAvt)
                                        }
                                        binding.searchSuccess.visibility = View.VISIBLE
                                        binding.noContact.visibility = View.GONE
                                        binding.searchSuccess.setOnClickListener{
                                            var intent = Intent(this@Search, ProfileActivity::class.java)
                                            startActivity(intent)
                                        }

                                        // TODO: Thực hiện xử lý với thông tin người dùng
                                    } else {
                                        // Xử lý trường hợp body của response là null
                                        Toast.makeText(this@Search, "phản hồi không thành công", Toast.LENGTH_SHORT).show();
                                        binding.noContact.visibility = View.VISIBLE
                                    }
                                } else {
                                    // Xử lý lỗi khi không thành công
                                    //  Toast.makeText(context, "yêu cầu không thành công", Toast.LENGTH_SHORT).show();
                                    binding.noContact.visibility = View.VISIBLE
                                    binding.searchSuccess.visibility = View.GONE
                                }
                            }

                            override fun onFailure(call: Call<GetUserByPhoneNumberResponse>, t: Throwable) {
                                // Xử lý khi có lỗi trong quá trình thực hiện yêu cầu
                                Toast.makeText(this@Search, "yêu cầu 2 không thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                    }else{
                        binding.frameSearch.visibility = View.GONE
                    }

                }

                return true
            }
        })

        binding.btnBack.setOnClickListener {
            onBackPressed()
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