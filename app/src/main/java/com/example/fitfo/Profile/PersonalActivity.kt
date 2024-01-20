package com.example.fitfo.Profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.postAdapter
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Logged
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.databinding.ActivityPersonalBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPersonalBinding
    private var listPosts: MutableList<GetPostReponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences =this.getSharedPreferences("data", Context.MODE_PRIVATE)
        binding.textName.setText(sharedPreferences.getString("MY_NAME",null).toString())
        val avatarUrl = sharedPreferences.getString("MY_AVATAR",null).toString()
        val myId = sharedPreferences.getString("MY_ID",null).toString()
        if (!avatarUrl.isNullOrEmpty() ) {
            ImageUtils.displayImage2(avatarUrl, binding.avatar)
        }


        binding.imgoption.setOnClickListener {
            var intent = Intent(this, OptionPersonal::class.java)
            startActivity( intent)
        }

        binding.backtoPeople.setOnClickListener {
            onBackPressed()
        }

        fetchPosts(myId)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Logged::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun fetchPosts(myId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findPostsOfUser(myId)
        call.enqueue(object : Callback<List<GetPostReponse>> {
            override fun onResponse(
                call: Call<List<GetPostReponse>>,
                response: Response<List<GetPostReponse>>
            ) {
                if (response.isSuccessful) {
                    val postsResponse = response.body()

                    if (!postsResponse.isNullOrEmpty()) {
                        listPosts.addAll(postsResponse);
                        val adapterDs = postAdapter(listPosts, myId)
                        var listpost = binding.rvListMyPost
                        listpost.layoutManager = LinearLayoutManager(this@PersonalActivity, LinearLayoutManager.VERTICAL, false)
                        listpost.adapter = adapterDs
                        listpost.setHasFixedSize(true)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(this@PersonalActivity, "bạn chưa có bài viết nào", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@PersonalActivity, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetPostReponse>>, t: Throwable) {
//                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@PersonalActivity, "errorMessage", Toast.LENGTH_LONG).show()
            }
        })
    }

}