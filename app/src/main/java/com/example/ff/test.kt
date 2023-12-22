package com.example.ff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ff.databinding.ActivityTestBinding
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import com.bumptech.glide.Glide

class test : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener { v -> showOptionsPopup(v) }

        val intent = intent
        if (intent.hasExtra("uriStory")) {
            val uriStory = intent.getStringExtra("uriStory")
            Log.d("fff", uriStory.toString())
            displaySelectedImage(uriStory.toString())
        }
    }

    private fun displaySelectedImage(imagePath: String) {
        Glide.with(this)
            .load(imagePath)
            .into(binding.imgtest)
    }

    private fun showOptionsPopup(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_option_story, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.option1 -> {

                    true
                }

                R.id.option2 -> {
//                    popupMenu.dismiss()
                    true
                }
                // Xử lý các Option khác nếu cần
                else -> false
            }
        }
        popupMenu.show()
    }
}