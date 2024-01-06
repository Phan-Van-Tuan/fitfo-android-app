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
import android.util.Log
import android.widget.Toast
import com.example.fitfo.Authentication.LoginActivity
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.Dialog
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Logged
import com.example.fitfo.Models.createPostRequest
import com.example.fitfo.Models.updateAvatarRequest
import com.example.fitfo.databinding.ActivityCreatePostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePost : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var photo: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString();

        binding.btnPickPhoto.setOnClickListener {
                ImageUtils.handleImagePick(this)
        }

        binding.btnPostAction.setOnClickListener {
            createPost(
                myId,
                binding.postCaption.text.toString().trim(),
                "post",
                photo)
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

    fun createPost( myId: String, caption: String, action: String, photo: String) {
        val apiService = RetrofitClient.apiService
        val createPostRequest = createPostRequest(myId, caption, action, photo)
        val call = apiService.createPost(createPostRequest)
        val dialog = Dialog()
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    dialog.showMessageDialog(
                        this@CreatePost,
                        "Success",
                        "Đăng bài thành công, chuyển hướng về trang nhật ký"
                    )
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({ onBackPressed() }, 3000)

                } else {
                    dialog.showMessageDialog(
                        this@CreatePost,
                        "Error",
                        "Đăng bài thất bại!"
                    )
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                dialog.showMessageDialog(
                    this@CreatePost,
                    "Error",
                    "Server hỏng rồi!"
                )
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
                val imageName = "posts/$currentTimeMillis.png" // Đặt tên mong muốn cho hình ảnh trên Firebase Storage

                ImageUtils.uploadImageToFirebaseStorage(this, selectedImageUri, imageName) { imageUrl ->
                    if (imageUrl != null) {
                        ImageUtils.displayImage2(imageUrl, binding.displayPostPhoto)
                        photo = imageUrl
                    } else {
                        Toast.makeText(this, "thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}