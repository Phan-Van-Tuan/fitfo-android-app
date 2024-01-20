package com.example.fitfo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.navigation.NavigationBarView
import androidx.fragment.app.Fragment
import com.example.fitfo.Fragment.ChatFragment
import com.example.fitfo.Fragment.ContactFragment
import com.example.fitfo.Fragment.DiaryFragment
import com.example.fitfo.Fragment.DiscoverFragment
import com.example.fitfo.Fragment.PersonalFragment
import com.example.fitfo.Define.MySocketManager
import com.example.fitfo.databinding.ActivityLoggedBinding
import com.jakewharton.threetenabp.AndroidThreeTen
import org.json.JSONObject


private lateinit var binding: ActivityLoggedBinding
class Logged : AppCompatActivity() {
    private val socketManager = MySocketManager()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AndroidThreeTen.init(this)

        sharedPreferences =this.getSharedPreferences("data",Context.MODE_PRIVATE)
        val id: String= sharedPreferences.getString("MY_ID","").toString()

        socketManager.initSocket()
        socketManager.connectSocket()
        socketManager.addNewUser(id)

        // socket lắng nghe
        socketManager.onReceiveNotification { args ->
            // Xử lý thông báo ở đây
            if (args.isNotEmpty()) {
                val notificationData = args[0] as JSONObject
                Log.d("notificationData_logged", ".............." + notificationData)
                // Thực hiện xử lý thông báo dựa trên dữ liệu nhận được từ server
                // Ví dụ: Hiển thị thông báo, cập nhật giao diện người dùng, v.v.
            }
        }

        val personalFragment = PersonalFragment()
        personalFragment.setSocketManager(socketManager)

        val chatFragment = ChatFragment()
        chatFragment.setSocketManager(socketManager)

        // display all title and content in bottom nav
        binding.bottomNavigationView.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED
        replaceFragment(chatFragment)

        binding.bottomNavigationView.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.bottom_chat -> replaceFragment(chatFragment)
                R.id.bottom_people -> replaceFragment(personalFragment)
                R.id.bottom_diary -> replaceFragment(DiaryFragment())
                R.id.bottom_tienich -> replaceFragment(DiscoverFragment())
                R.id.bottom_profile -> replaceFragment(ContactFragment())
                else -> {
                    replaceFragment(chatFragment)
                }
            }
            true
        }

    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ngắt kết nối socket khi activity bị hủy
        socketManager?.disconnectSocket()
    }
}