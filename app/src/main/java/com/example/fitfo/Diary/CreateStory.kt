package com.example.fitfo.Diary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.Dialog
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Logged
import com.example.fitfo.Models.createStoryRequest
import com.example.fitfo.databinding.ActivityCreateStoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateStory : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var photo: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString();

        ImageUtils.handleImagePick(this)


        binding.btnStoryAction.setOnClickListener {
            createStory(myId, photo)
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Logged::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun createStory( myId: String, photo: String) {
        val apiService = RetrofitClient.apiService
        val createStoryRequest = createStoryRequest(myId, photo)
        val call = apiService.createStory(createStoryRequest)
        val dialog = Dialog()
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    dialog.showMessageDialog(this@CreateStory, "Success", "Đăng tin thành công, chuyển hướng về trang nhật ký")
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({ onBackPressed() }, 3000)

                } else {
                    dialog.showMessageDialog(this@CreateStory, "Error", "Đăng tin thất bại!")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                dialog.showMessageDialog(this@CreateStory, "Error", "Server hỏng rồi!")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageUtils.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Uri of the selected image
            val selectedImageUri: Uri? = data?.data

            // Call uploadImageToFirebaseStorage function with this Uri
            if (selectedImageUri != null) {
                val currentTimeMillis = System.currentTimeMillis()
                val imageName = "story/$currentTimeMillis.png" // Đặt tên mong muốn cho hình ảnh trên Firebase Storage

                ImageUtils.uploadImageToFirebaseStorage(this, selectedImageUri, imageName) { imageUrl ->
                    if (imageUrl != null) {
                        ImageUtils.displayImage2(imageUrl, binding.displayStoryPhoto)
                        photo = imageUrl
                    } else {
                        Toast.makeText(this, "thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}