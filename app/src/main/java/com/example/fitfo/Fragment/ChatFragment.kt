package com.example.fitfo.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ListChatAdapter
import com.example.fitfo.ChatActivity
import com.example.fitfo.Interface.ApiService
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.findChatResponse
import com.example.fitfo.Test.MyInfo
import com.example.fitfo.databinding.FragmentChatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private var listChats: MutableList<findChatResponse> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        var sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString();
        fetchChats(myId);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun fetchChats(myId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fitfo-api.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.findChats(myId)
        call.enqueue(object : Callback<List<findChatResponse>> {
            override fun onResponse(
                call: Call<List<findChatResponse>>,
                response: Response<List<findChatResponse>>
            ) {
                if (response.isSuccessful) {
                    val chatsResponse = response.body()

                    if (!chatsResponse.isNullOrEmpty()) {
                        listChats.addAll(chatsResponse);

                        val adapterDs = ListChatAdapter(listChats, object : RvChat {

                            override fun onClickchat(pos: Int) {
                                MyInfo.userName = listChats[pos].chatName;
                                MyInfo.chatID = listChats[pos]._id;
                                MyInfo.userID = listChats[pos].members[1];
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

            override fun onFailure(call: Call<List<findChatResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

}