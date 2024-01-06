package com.example.fitfo.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ContactAdapter
import com.example.fitfo.ChatActivity
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Models.GetUserByPhoneNumberResponse
import com.example.fitfo.Profile.ProfileActivity
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.getListFriendResponse
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentContactBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactBinding
    private var listContacts: MutableList<getListFriendResponse> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        call.enqueue(object : Callback<List<getListFriendResponse>> {
            override fun onResponse(
                call: Call<List<getListFriendResponse>>,
                response: Response<List<getListFriendResponse>>
            ) {
                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        listContacts.addAll(response.body()!!);
                        val contactAdapter = ContactAdapter(listContacts, object : RvChat {
                            override fun onClickchat(pos: Int) {
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
                        Toast.makeText(context, "Lỗi 178", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
//                    val error = response.errorBody()?.string()
                    Toast.makeText(context, "Lỗi 179", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<getListFriendResponse>>, t: Throwable) {
                Toast.makeText(context, "Lỗi 180", Toast.LENGTH_LONG).show()
            }
        })
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}