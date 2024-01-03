package com.example.ff

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ff.Interface.ApiService
import com.example.ff.Models.findChatResponse
import com.example.ff.Test.MyInfo
import com.example.ff.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()
        val userId = MyInfo.userID
        binding.txtName.setText(MyInfo.userName)
        fetchChat(myId, userId)

    }

    private fun fetchChat(myId: String, userId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fitfo-api.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.findChat(myId, userId)
        call.enqueue(object : Callback<List<findChatResponse>> {
            override fun onResponse(
                call: Call<List<findChatResponse>>,
                response: Response<List<findChatResponse>>
            ) {
                if (response.isSuccessful) {
                    val chatResponses = response.body()
                    var chatId: String = "";
//                    var chatMember: List<String>? = null;
                    if (!chatResponses.isNullOrEmpty()) {
                        val firstChatResponse = chatResponses[0]
                        chatId = firstChatResponse._id;
//                        chatMember = firstChatResponse.members;
                    }
                    Toast.makeText(
                        this@ProfileActivity,
                        "chatID: $chatId",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.btnToChat.setOnClickListener {
                        MyInfo.chatID = chatId
                        var intent = Intent(this@ProfileActivity, ChatActivity::class.java)
                        startActivity(intent);
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<findChatResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}