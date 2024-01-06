package com.example.fitfo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import androidx.fragment.app.Fragment
import com.example.fitfo.Fragment.ChatFragment
import com.example.fitfo.Fragment.ContactFragment
import com.example.fitfo.Fragment.DiaryFragment
import com.example.fitfo.Fragment.DiscoverFragment
import com.example.fitfo.Fragment.PersonalFragment
import com.example.fitfo.Define.MySocketManager
import com.example.fitfo.databinding.ActivityLoggedBinding


private lateinit var binding: ActivityLoggedBinding
class Logged : AppCompatActivity() {

    private val socketManager = MySocketManager()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.fitfo.binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences =this.getSharedPreferences("data",Context.MODE_PRIVATE)
        val id: String= sharedPreferences.getString("MY_ID","").toString()


        socketManager.initSocket()
        socketManager.connectSocket()
        socketManager.addNewUser(id)
        // khởi tạo đối tượng dialog
        // display all title and content in bottom nav
        com.example.fitfo.binding.bottomNavigationView.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED
        replaceFragment(ChatFragment())


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

// Ẩn tiêu đề của mục "Add"

//       bottomNavigationView.getOrCreateBadge(R.id.bottom_chat).isVisible = false

        com.example.fitfo.binding.bottomNavigationView.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.bottom_chat -> replaceFragment(ChatFragment())
                R.id.bottom_people -> replaceFragment(PersonalFragment())
                R.id.bottom_diary -> replaceFragment(DiaryFragment())
                R.id.bottom_tienich -> replaceFragment(DiscoverFragment())
                R.id.bottom_profile -> replaceFragment(ContactFragment())
                else -> {
                    replaceFragment(ChatFragment())
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
}