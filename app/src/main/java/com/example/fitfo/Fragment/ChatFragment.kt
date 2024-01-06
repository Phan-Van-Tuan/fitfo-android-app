package com.example.fitfo.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ListChatAdapter
import com.example.fitfo.ChatActivity
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.findChatResponse
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Models.GetUserByPhoneNumberResponse
import com.example.fitfo.Profile.ProfileActivity
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentChatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                val phoneNumberInsert = newText?.trim()
//
//                if (phoneNumberInsert != null) {
//                    if (phoneNumberInsert.length==10){
//                        binding.dsChat.visibility = View.GONE
//                        binding.frameSearch.visibility = View.VISIBLE
//                        val apiService = RetrofitClient.apiService
//                        val phoneNumber = newText ?: ""
//
//                        val call = apiService.getUserByPhoneNumber(phoneNumber)
//                        call.enqueue(object : Callback<GetUserByPhoneNumberResponse> {
//                            override fun onResponse(
//                                call: Call<GetUserByPhoneNumberResponse>,
//                                response: Response<GetUserByPhoneNumberResponse>
//                            ) {
//                                if (response.isSuccessful) {
//                                    val userResponse = response.body()
//
//                                    if (userResponse != null) {
//                                        val userId = userResponse.id
//                                        val userName = userResponse.name
//                                        val userAvatar = userResponse.avatar
//                                        val userPhoneNumber = userResponse.phoneNumber
//                                        UserInfo.userName= userName
//                                        UserInfo.userID= userId
//                                        UserInfo.userAvatar = userAvatar
//                                        binding.textName.setText(userName)
//                                        if (!userAvatar.isNullOrEmpty() ) {
//                                            ImageUtils.displayImage2(userAvatar, binding.imgAvt)
//                                        }
//                                        binding.searchSuccess.visibility = View.VISIBLE
//                                        binding.noContact.visibility = View.GONE
//                                        binding.searchSuccess.setOnClickListener{
//                                            var intent = Intent(context, ProfileActivity::class.java)
//                                            startActivity(intent)
//                                        }
//
//                                        // TODO: Thực hiện xử lý với thông tin người dùng
//                                    } else {
//                                        // Xử lý trường hợp body của response là null
//                                        Toast.makeText(context, "phản hồi không thành công", Toast.LENGTH_SHORT).show();
//                                        binding.noContact.visibility = View.VISIBLE
//                                    }
//                                } else {
//                                    // Xử lý lỗi khi không thành công
//                                    //  Toast.makeText(context, "yêu cầu không thành công", Toast.LENGTH_SHORT).show();
//                                    binding.noContact.visibility = View.VISIBLE
//                                    binding.searchSuccess.visibility = View.GONE
//                                }
//                            }
//
//                            override fun onFailure(call: Call<GetUserByPhoneNumberResponse>, t: Throwable) {
//                                // Xử lý khi có lỗi trong quá trình thực hiện yêu cầu
//                                Toast.makeText(context, "yêu cầu 2 không thành công", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                    }else{
//                        binding.frameSearch.visibility =View.GONE
//                    }
//
//                }
//
//                return true
//            }
//        })
    }

    private fun fetchChats(myId: String) {
        val apiService = RetrofitClient.apiService
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

                        val adapterDs = ListChatAdapter(listChats,  object : RvChat {

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

            override fun onFailure(call: Call<List<findChatResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

}