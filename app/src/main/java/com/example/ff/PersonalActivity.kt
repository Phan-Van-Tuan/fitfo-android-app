package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.Fragment.PersonalFragment
import com.example.ff.OptionActivity.OptionPersonal
import com.example.ff.databinding.ActivityPersonalBinding

class PersonalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
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