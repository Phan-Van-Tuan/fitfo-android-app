package com.example.fitfo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitfo.Fragment.PersonalFragment
import com.example.fitfo.OptionActivity.OptionPersonal
import com.example.fitfo.databinding.ActivityPersonalBinding

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