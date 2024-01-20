package com.example.fitfo.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ContactAdapter
import com.example.fitfo.ChatActivity
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Interface.RecyclerViewOnClick
import com.example.fitfo.Models.ListFriendResponse
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentContactBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactBinding
    private var listContacts: MutableList<ListFriendResponse> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }

        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()
        fetchListFriends(myId)
    }

    private fun fetchListFriends(myId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findContact(myId)
        call.enqueue(object : Callback<List<ListFriendResponse>> {
            override fun onResponse(
                call: Call<List<ListFriendResponse>>,
                response: Response<List<ListFriendResponse>>
            ) {
                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        listContacts.addAll(response.body()!!);
                        listContacts.sortBy { it.name }
                        val contactAdapter = ContactAdapter(listContacts, object : RecyclerViewOnClick {
                            override fun onClickItem(pos: Int) {
                                var intent = Intent(context, ChatActivity::class.java)
                                UserInfo.userName= listContacts[pos].name
                                UserInfo.userID= listContacts[pos].userId
                                UserInfo.userAvatar = listContacts[pos].avatar
                                UserInfo.chatID = listContacts[pos].chatId
                                startActivity(intent)
                            }
                        })
                        var listcontact = binding.listcontact
                        listcontact.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        listcontact.adapter = contactAdapter
                        listcontact.setHasFixedSize(true)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(context, "Chưa có bạn bè", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
//                    val error = response.errorBody()?.string()
                    Toast.makeText(context, "Lỗi 179", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ListFriendResponse>>, t: Throwable) {
                Toast.makeText(context, "Lỗi 180", Toast.LENGTH_LONG).show()
            }
        })
    }

}