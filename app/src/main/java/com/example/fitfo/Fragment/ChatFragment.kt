package com.example.fitfo.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ListChatAdapter
import com.example.fitfo.ChatActivity
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.MySocketManager
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Models.ChatResponse
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentChatBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

@Suppress("UNREACHABLE_CODE")
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private var listChats: MutableList<ChatResponse> = mutableListOf()
    private var socketManager: MySocketManager? = null

    fun setSocketManager(value: MySocketManager) {
        socketManager = value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        var sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString();
        fetchChats(myId);
        return binding.root

        socketManager?.onReceiveNotification { args ->
            // Xử lý thông báo ở đây
            if (args.isNotEmpty()) {
                val notificationData = args[0] as JSONObject
                Log.d("notificationData...........", ".............." + notificationData)
                // Thực hiện xử lý thông báo dựa trên dữ liệu nhận được từ server
                // Ví dụ: Hiển thị thông báo, cập nhật giao diện người dùng, v.v.

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }
    }

    private fun fetchChats(myId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findChats(myId)
        call.enqueue(object : Callback<List<ChatResponse>> {
            override fun onResponse(
                call: Call<List<ChatResponse>>,
                response: Response<List<ChatResponse>>
            ) {
                if (response.isSuccessful) {
                    val chatsResponse = response.body()

                    if (!chatsResponse.isNullOrEmpty()) {
                        listChats.addAll(chatsResponse);
                        listChats.sortWith(Comparator.comparing(ChatResponse::latestSend))
                        Collections.reverse(listChats);
                        val adapterDs = ListChatAdapter(listChats, myId,object : RvChat {
                            override fun onClickchat(pos: Int) {
                                UserInfo.userName = listChats[pos].chatName;
                                UserInfo.userAvatar = listChats[pos].chatAvatar;
                                UserInfo.chatID = listChats[pos]._id;
                                val members = listChats[pos].members

                                if (members.size == 2) {
                                    UserInfo.userID = if (members[0] == myId) {
                                        members[1]
                                    } else {
                                        members[0]
                                    }
                                } else {
                                    // Xử lý khi mảng members không đúng điều kiện
                                    // Ví dụ: UserInfo.userID = ""
                                }
                                var intent = Intent(context, ChatActivity::class.java)
                                intent.putExtra("idChat","${listChats[pos]._id}")
                                startActivity(intent)
                            }
                        })
                        var dsChat = binding.dsChat
                        dsChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                        dsChat.adapter = adapterDs
                        dsChat.setHasFixedSize(true)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(context, "Không có chat nào", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(context, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ChatResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

}