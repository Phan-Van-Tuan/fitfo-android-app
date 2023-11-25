package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ff.databinding.ActivityAccountSecurityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class Account_SecurityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSecurityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backtopeople.setOnClickListener {
            var intent = Intent(this, MainActivity_Logged_in::class.java)
            startActivity(intent)
//            replaceFragment(PeopleActivity())
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}