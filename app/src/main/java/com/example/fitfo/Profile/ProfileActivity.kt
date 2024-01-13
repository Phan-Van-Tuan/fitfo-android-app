package com.example.fitfo.Profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.postAdapter
import com.example.fitfo.ChatActivity
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Logged
import com.example.fitfo.Models.ChatResponse
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.Models.addFriendshipRequest
import com.example.fitfo.Models.FriendShipResponse
import com.example.fitfo.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var friendship: Int = 0
    private var friendshipID: String = ""
    private var changefriendship: Int = 0
    private var listPosts: MutableList<GetPostReponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()
        val userId = UserInfo.userID
        val userAvatar = UserInfo.userAvatar

        if (!userAvatar.isNullOrEmpty() ) {
            ImageUtils.displayImage2(userAvatar, binding.avatar)
        }

        // tình trạng bạn bè
        fetchFriendship(myId,userId)
        fetchPosts(userId, myId)

        binding.textName.setText(UserInfo.userName)
        fetchChat(myId, userId)

        binding.backtoPeople.setOnClickListener {
            handleFriendShip(friendship, changefriendship, myId, userId, friendshipID)
            onBackPressed()
        }
        clickbtn()
    }

    private fun fetchChat(myId: String, userId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findChat(myId, userId)
        call.enqueue(object : Callback<List<ChatResponse>> {
            override fun onResponse(
                call: Call<List<ChatResponse>>,
                response: Response<List<ChatResponse>>
            ) {
                if (response.isSuccessful) {
                    val chatResponses = response.body()
                    var chatId: String = "";
                    if (!chatResponses.isNullOrEmpty()) {
                        val firstChatResponse = chatResponses[0]
                        chatId = firstChatResponse._id;
                    }

                    binding.btnToChat.setOnClickListener {
                        UserInfo.chatID = chatId
                        handleFriendShip(friendship, changefriendship, myId, userId, friendshipID)
                        var intent = Intent(this@ProfileActivity, ChatActivity::class.java)
                        startActivity(intent);
                        finish()
                    }
                } else {
                    val error = response.errorBody()?.string()
//                    Toast.makeText(this@ProfileActivity, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ChatResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchPosts(userId: String, myId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findPostsOfUser(userId)
        call.enqueue(object : Callback<List<GetPostReponse>> {
            override fun onResponse(
                call: Call<List<GetPostReponse>>,
                response: Response<List<GetPostReponse>>
            ) {
                if (response.isSuccessful) {
                    val postsResponse = response.body()

                    if (!postsResponse.isNullOrEmpty()) {
                        Toast.makeText(this@ProfileActivity, "Success", Toast.LENGTH_SHORT).show()
                        listPosts.addAll(postsResponse);
                        val adapterDs = postAdapter(listPosts, myId)
                        var listpost = binding.rvListUserPost
                        listpost.layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.VERTICAL, false)
                        listpost.adapter = adapterDs
                        listpost.setHasFixedSize(true)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        if(friendship == 3) {
                            binding.recyclerviewNoPost.setText(
                                "${UserInfo.userName} chưa có bài viết nào. vào tin nhắn bảo ổng đăng bài đi")
                        }

                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetPostReponse>>, t: Throwable) {
//                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@ProfileActivity, "errorPost", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchFriendship(myId: String, userId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.getFriendship(myId, userId)
        call.enqueue(object : Callback<FriendShipResponse> {
            override fun onResponse(
                call: Call<FriendShipResponse>,
                response: Response<FriendShipResponse>
            ) {
                if (response.isSuccessful) {
                    val user1 = response.body()?.user1
                    val user2 = response.body()?.user2
                    friendshipID = response.body()?._id.toString()
                    val accepted = response.body()?.accepted.toBoolean()
                    if(accepted) {
                        friendship = 3
                        binding.btnAddFriend.visibility = View.GONE
                        binding.btnDestroyFriendship.visibility = View.VISIBLE
                        binding.rvListUserPost.visibility = View.VISIBLE
                        binding.recyclerviewNoPost.visibility = View.GONE
                    } else if (user1 == myId){
                        friendship = 1
                        binding.btnAddFriend.visibility = View.GONE
                        binding.btnWaitAcceptFriendship.visibility = View.VISIBLE
                    } else {
                        friendship = 2
                        binding.btnAddFriend.visibility = View.GONE
                        binding.btnAcceptFriendship.visibility = View.VISIBLE
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Lỗi3: $error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<FriendShipResponse>, t: Throwable) {
                val errorMessage = "Lỗi2: ${t.message}"
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun clickbtn() {
        binding.btnAcceptFriendship.setOnClickListener{
            changefriendship = 3
            binding.btnAddFriend.visibility = View.GONE
            binding.btnAcceptFriendship.visibility = View.GONE
            binding.btnWaitAcceptFriendship.visibility = View.GONE
            binding.btnDestroyFriendship.visibility = View.VISIBLE
            binding.rvListUserPost.visibility = View.VISIBLE
        }
        binding.btnDestroyFriendship.setOnClickListener {
            changefriendship = 0
            deletefriendShip(friendshipID)
            binding.btnAddFriend.visibility = View.VISIBLE
            binding.btnAcceptFriendship.visibility = View.GONE
            binding.btnWaitAcceptFriendship.visibility = View.GONE
            binding.btnDestroyFriendship.visibility = View.GONE
        }
        binding.btnWaitAcceptFriendship.setOnClickListener {
            changefriendship = 0
            deletefriendShip(friendshipID)
            binding.btnAddFriend.visibility = View.VISIBLE
            binding.btnAcceptFriendship.visibility = View.GONE
            binding.btnWaitAcceptFriendship.visibility = View.GONE
            binding.btnDestroyFriendship.visibility = View.GONE
        }
        binding.btnAddFriend.setOnClickListener {
            changefriendship = 1
            binding.btnAddFriend.visibility = View.GONE
            binding.btnAcceptFriendship.visibility = View.GONE
            binding.btnWaitAcceptFriendship.visibility = View.VISIBLE
            binding.btnDestroyFriendship.visibility = View.GONE
        }
    }

    private fun handleFriendShip(
        friendship: Int,
        changeFriendship: Int,
        myId: String,
        userId: String,
        friendshipId: String,
    ) {
        if(friendship == changeFriendship) return
        if(changeFriendship == 1) createfriendShip(myId, userId)
        if(changeFriendship == 3) acceptfriendShip(friendshipId)
    }

    private fun deletefriendShip(friendshipId : String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.deleteFriend(friendshipId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "delete ok", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("Lỗi delete 1", "$error")
                    Toast.makeText(this@ProfileActivity, "Lỗi delete 1: $error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                val errorMessage = "Lỗi delete 2: ${t.message}"
                Log.e("Lỗi delete 2", "${t.message}")
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun acceptfriendShip(friendshipId : String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.acceptFriend(friendshipId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "accept ok", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Lỗi accept 1: $error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                val errorMessage = "Lỗi accept 2: ${t.message}"
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createfriendShip(myId: String, userId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.addFriend(addFriendshipRequest(myId, userId))
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "create ok", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Lỗi create 1: $error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                val errorMessage = "Lỗi create 2: ${t.message}"
                Toast.makeText(this@ProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Logged::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}