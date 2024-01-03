package com.example.fitfo.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ContactAdapter
import com.example.fitfo.Interface.ApiService
import com.example.fitfo.Models.GetUserByPhoneNumberResponse
import com.example.fitfo.OutData.OutDataContact
import com.example.fitfo.ProfileActivity
import com.example.fitfo.R
import com.example.fitfo.Test.MyInfo
import com.example.fitfo.databinding.FragmentContactBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactBinding
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
        val contact = mutableListOf<OutDataContact>()
        contact.add(OutDataContact(R.drawable.a, "Tuấn"))
        contact.add(OutDataContact(R.drawable.a, "Tuấn"))
        contact.add(OutDataContact(R.drawable.a, "Tuấn"))
        contact.add(OutDataContact(R.drawable.a, "Tuấn"))

        val adaptercontact = ContactAdapter(contact)
        var listcontact = binding.listcontact
        listcontact.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listcontact.adapter = adaptercontact
        listcontact.setHasFixedSize(true)
//        Log.e("adapter", binding.dsChat.toString())
//        Toast.makeText(context, ds[0].txtName, Toast.LENGTH_LONG).show()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val phoneNumberInsert = newText?.trim()

                if (phoneNumberInsert != null) {
                    if (phoneNumberInsert.length==10){
                        binding.listcontact.visibility = View.GONE
                        binding.frameSearch.visibility = View.VISIBLE
                        val retrofit = Retrofit.Builder()
                            .baseUrl("https://fitfo-api.vercel.app/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                        val apiService = retrofit.create(ApiService::class.java)

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
                                        val userPhoneNumber = userResponse.phoneNumber
                                        MyInfo.userName= userName
                                        MyInfo.userID= userId
                                        binding.txtName.setText(userName)
                                        binding.searchSuccess.visibility = View.VISIBLE
                                        binding.noContact.visibility = View.GONE
                                        binding.searchSuccess.setOnClickListener{
                                            var intent = Intent(context, ProfileActivity::class.java)
                                            startActivity(intent)
                                        }

                                        // TODO: Thực hiện xử lý với thông tin người dùng
                                    } else {
                                        // Xử lý trường hợp body của response là null
                                        Toast.makeText(context, "phản hồi không thành công", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "yêu cầu 2 không thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                    }else{
                        binding.frameSearch.visibility =View.GONE
                        binding.listcontact.visibility = View.VISIBLE
                    }

                }

                return true
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