package com.example.ff.OptionActivity

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ff.ChatActivity
import com.example.ff.PersonalActivity
import com.example.ff.databinding.ActivityOptionPersonalBinding
import com.squareup.picasso.Picasso
import java.io.IOException

fun Uri.toBitmap(contentResolver: ContentResolver): Bitmap? {
    return try {
        contentResolver.openInputStream(this)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

class OptionPersonal : AppCompatActivity() {
    private lateinit var binding: ActivityOptionPersonalBinding
    val REQUEST_CODE = 10
    val GALLERY_REQUEST_CODE = 15

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                handleImageUri(it)
            }
        }

    private fun handleImageUri(uri: Uri) {
        // Xử lý đường dẫn của ảnh ở đây
        val bitmap: Bitmap? = uri?.toBitmap(this.contentResolver)
        if (bitmap != null) {
            // Hiển thị hoặc xử lý ảnh theo nhu cầu của bạn
            val imageView: ImageView = binding.imgpick
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBacktoPersonal.setOnClickListener {
            var intent = Intent(this, PersonalActivity::class.java)
            startActivity(intent)
        }
        binding.txtEditAvt.setOnClickListener {
            onClickRequestPermission()
        }
    }


    private fun onClickRequestPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Quyền đã được cấp, thực hiện công việc liên quan đến thư viện ở đây
            openGallery()
        } else {
            // Quyền chưa được cấp, yêu cầu quyền
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
        }

    }

    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)

        //cách 2  dùng launcher
        // Sử dụng launcher để mở thư viện (gallery)
        galleryLauncher.launch("image/*")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform tasks related to the gallery here
                openGallery()
            } else {
                // Permission not granted, handle it (e.g., show a message to the user)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            // Handle the selected image from the gallery
// requestCode là một mã số đặc biệt được gán khi bạn gọi một hoạt động (activity)
            // để mở thư viện ảnh bằng startActivityForResult hoặc launcher.launch (nếu sử dụng ActivityResultLauncher).
//GALLERY_REQUEST_CODE là một hằng số mà bạn tự đặt để định danh cho yêu cầu mở thư viện ảnh.
            val selectedImageUri = data?.data
            // Continue processing based on your needs
            try {

                // Chuyển đổi đường dẫn thành đối tượng Bitmap
                val bitmap: Bitmap? = selectedImageUri?.let { uri ->
                    this.contentResolver.openInputStream(uri)?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                }

                // Kiểm tra xem bitmap có null hay không trước khi sử dụng
                if (bitmap != null) {
                    // Sử dụng bitmap theo nhu cầu của bạn
                    // Ví dụ: Hiển thị ảnh trên ImageView
                    val imageView: ImageView = binding.imgpick
                    imageView.setImageBitmap(bitmap)

                    // Hoặc tiếp tục xử lý ảnh theo nhu cầu của bạn
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}