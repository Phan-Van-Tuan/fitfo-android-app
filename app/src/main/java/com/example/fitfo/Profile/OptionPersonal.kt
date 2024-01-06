package com.example.fitfo.Profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Models.updateAvatarRequest
import com.example.fitfo.databinding.ActivityOptionPersonalBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OptionPersonal : AppCompatActivity() {
    private lateinit var binding: ActivityOptionPersonalBinding;
    private lateinit var sharedPreferences: SharedPreferences;
    private lateinit var myId: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // hiển thị tên
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        myId = sharedPreferences.getString("MY_ID", null).toString();
        val myName = sharedPreferences.getString("MY_NAME", null).toString();
        binding.textName.text = myName;

        // trở về màn hình trước
        binding.imgBacktoPersonal.setOnClickListener {
            var intent = Intent(this, PersonalActivity::class.java)
            startActivity(intent);
            finish();
        }

        // click vào đổi ảnh đại diện
        binding.txtEditAvt.setOnClickListener {
            ImageUtils.handleImagePick(this);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageUtils.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Uri of the selected image
            val selectedImageUri: Uri? = data?.data

            // Call uploadImageToFirebaseStorage function with this Uri
            if (selectedImageUri != null) {
                val imageName = "avatars/$myId.png" // Đặt tên mong muốn cho hình ảnh trên Firebase Storage

                ImageUtils.uploadImageToFirebaseStorage(this, selectedImageUri, imageName) { imageUrl ->
                    if (imageUrl != null) {
//                        ImageUtils.displayImage2(imageUrl, binding.imgpick)
                        val apiService = RetrofitClient.apiService;
                        val call = apiService.updateAvatar(myId, updateAvatarRequest(imageUrl))
                        call.enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@OptionPersonal, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                                    val editor = sharedPreferences.edit()
                                    editor.putString("MY_AVATAR", imageUrl)
                                    editor.apply()
                                } else {
                                    val error = response.errorBody()?.string()
//                                    Toast.makeText(this@OptionPersonal, "error: "+ error, Toast.LENGTH_SHORT).show();
                                    Log.e("error", error.toString())
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                // Show an error message in case of failure
                            }
                        })

                    } else {
                        Toast.makeText(this, "Cập nhật ảnh đại diện thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}