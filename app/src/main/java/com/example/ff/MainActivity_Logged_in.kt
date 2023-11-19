package com.example.ff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.databinding.ActivityMainBinding
import com.example.ff.databinding.ActivityMainLoggedInBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import androidx.fragment.app.Fragment
import com.example.ff.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private lateinit var binding: ActivityMainLoggedInBinding
class MainActivity_Logged_in : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.ff.binding = ActivityMainLoggedInBinding.inflate(layoutInflater)
        setContentView(com.example.ff.binding.root)
        // khởi tạo đối tượng dialog
        // display all title and content in bottom nav
        com.example.ff.binding.bottomNavigationView.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

// Ẩn tiêu đề của mục "Add"
//       bottomNavigationView.getOrCreateBadge(R.id.bottom_chat).isVisible = false

        com.example.ff.binding.bottomNavigationView.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.bottom_chat -> replaceFragment(homeFragment())
                R.id.bottom_people -> replaceFragment(people())
                R.id.bottom_diary -> replaceFragment(nhatky())
//                R.id.bottom_library -> replaceFragment(Library())
//                R.id.bottom_edit_account -> replaceFragment(Profile())
                else -> {
                    replaceFragment(homeFragment())
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