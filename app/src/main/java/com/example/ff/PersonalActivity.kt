package com.example.ff

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ff.Fragment.PersonalFragment
import com.example.ff.OptionActivity.OptionPersonal
import com.example.ff.databinding.ActivityPersonalBinding
import com.example.ff.MainActivity_Logged_in

class PersonalActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPersonalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences =this.getSharedPreferences("data", Context.MODE_PRIVATE)
        binding.txtName.setText(sharedPreferences.getString("MY_NAME",null).toString())
        binding.imgoption.setOnClickListener {
            var intent = Intent(this, OptionPersonal::class.java)
            startActivity( intent)
        }

        binding.backtoPeople.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout,PersonalFragment())
            fragmentTransaction.commit()
        }

    }

}