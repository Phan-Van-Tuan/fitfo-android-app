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
import com.example.fitfo.Interface.RecyclerViewOnClick
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Models.ChatResponse
import com.example.fitfo.Models.OutData
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentChatBinding
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

@Suppress("UNREACHABLE_CODE")
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapterListChat: ListChatAdapter
    private var listChats: MutableList<ChatResponse> = mutableListOf()
    private var socketManager: MySocketManager? = null

    fun setSocketManager(value: MySocketManager) {
        socketManager = value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        var sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()
        listChats = mutableListOf()
        fetchChats(myId)
        if (::adapterListChat.isInitialized) {
            activity?.runOnUiThread {
                adapterListChat.notifyDataSetChanged()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }

        socketManager?.onReceiveNotification { args ->
            // Xử lý thông báo ở đây
            if (args.isNotEmpty()) {
                val notificationData = args[0] as JSONObject
                Log.d("notificationDataFrag", "data" + notificationData)
                val chatResponse = Gson().fromJson(notificationData.toString(), OutData::class.java)
                // Thực hiện xử lý thông báo dựa trên dữ liệu nhận được từ server
                // Ví dụ: Hiển thị thông báo, cập nhật giao diện người dùng, v.v.
                updateChatList(listOf(chatResponse),listChats)
                activity?.runOnUiThread {
                    adapterListChat.notifyDataSetChanged()
                }
            }
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
                        listChats.addAll(chatsResponse)
                        listChats.sortWith(Comparator.comparing(ChatResponse::latestSend))
                        Collections.reverse(listChats)
                        adapterListChat = ListChatAdapter(listChats, myId,object : RecyclerViewOnClick {
                            override fun onClickItem(pos: Int) {
                                UserInfo.userName = listChats[pos].chatName
                                UserInfo.userAvatar = listChats[pos].chatAvatar
                                UserInfo.chatID = listChats[pos]._id
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
                                readChat(listChats[pos]._id)
                                var intent = Intent(context, ChatActivity::class.java)
                                intent.putExtra("idChat","${listChats[pos]._id}")
                                startActivity(intent)
                            }
                        })
                        var dsChat = binding.dsChat
                        dsChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                        dsChat.adapter = adapterListChat
                        dsChat.setHasFixedSize(true)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(context, "Chưa có chat nào", Toast.LENGTH_SHORT)
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

    fun updateChatList(chatsResponse: List<OutData>, listChats: MutableList<ChatResponse>) {
        for (newChat in chatsResponse) {
            var found = false
            for (i in listChats.indices) {
                val existingChat = listChats[i]
                if (newChat.chatId.equals(existingChat._id)) {
                    // Nếu đã tồn tại, cập nhật thông tin
                    val updateChat = mutableListOf<ChatResponse>()
                    updateChat.add(ChatResponse(
                        existingChat._id,
                        newChat.isRead,
                        existingChat.chatAvatar,
                        existingChat.chatName,
                        existingChat.members,
                        newChat.latestSend,
                        newChat.latestType,
                        newChat.latestMessage,
                        newChat.latestSenderId))
                    listChats[i] = updateChat[0]
                    found = true
                    break
                }
            }
            if (!found) {
                // Nếu không tồn tại, thêm mới vào danh sách
            }
        }

        // Sắp xếp danh sách theo thời gian gửi mới nhất
        listChats.sortWith(Comparator.comparing(ChatResponse::latestSend).reversed())
    }

    private fun readChat(chatId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.readChat(chatId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (!response.isSuccessful) {
                    val error = response.errorBody()?.string()
                    Log.e("error 358", "$error")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Log.e("error 359", "$errorMessage")
            }
        })
    }

}