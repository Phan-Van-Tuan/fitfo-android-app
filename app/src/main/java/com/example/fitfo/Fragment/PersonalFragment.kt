package com.example.fitfo.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitfo.Profile.Account_Security
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.MySocketManager
import com.example.fitfo.MainActivity
import com.example.fitfo.Profile.PersonalActivity
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentPersonalBinding

class PersonalFragment : Fragment() {
    private lateinit var binding: FragmentPersonalBinding
    private lateinit var sharedPreferences: SharedPreferences
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
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }

        binding.personal.setOnClickListener {
            var intent = Intent(context, PersonalActivity::class.java)
            startActivity(intent)
        }
        binding.accountAndSecurity.setOnClickListener {
            var intent = Intent(context, Account_Security::class.java)
            startActivity(intent)
        }

        binding.btnLogOut.setOnClickListener {
            removePrivateData()
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            socketManager?.disconnectSocket()
            onDestroy()
        }

        sharedPreferences = requireActivity()?.getSharedPreferences("data", Context.MODE_PRIVATE)!!
        val avatarUrl = sharedPreferences.getString("MY_AVATAR", "")
        if (!avatarUrl.isNullOrEmpty() ) {
            ImageUtils.displayImage2(avatarUrl, binding.imgAvt)
        }
        binding.textName.setText(sharedPreferences.getString("MY_NAME", ""))

    }
    fun removePrivateData() {
        sharedPreferences = requireActivity()?.getSharedPreferences("data", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()

        editor.remove("MY_ID")
        editor.remove("MY_NAME")
        editor.remove("MY_PHONE_NUMBER")
        editor.remove("MY_AVATAR")
        editor.remove("ACCESS_TOKEN")
        editor.apply()
    }
}