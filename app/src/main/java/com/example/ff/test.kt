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
    private val PICK_IMAGE_REQUEST = 123
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        binding.button2.setOnClickListener { v -> showOptionsPopup(v) }
//
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//
//        val intent = intent
//        if (intent.hasExtra("uriStory")) {
//            val uriStory = intent.getStringExtra("uriStory")
//            Log.d("fff", uriStory.toString())
//            displaySelectedImage(uriStory.toString())
//        }
//    }
//
//    private fun displaySelectedImage(imagePath: String) {
//        Glide.with(this)
//            .load(imagePath)
//            .into(binding.imgtest)
//    }
//
//    private fun showOptionsPopup(view: View) {
//        val popupMenu = PopupMenu(this, view)
//        popupMenu.menuInflater.inflate(R.menu.menu_option_story, popupMenu.menu)
//
//        popupMenu.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.option1 -> {
//
//                    true
//                }
//
//                R.id.option2 -> {
////                    popupMenu.dismiss()
//                    true
//                }
//                // Xử lý các Option khác nếu cần
//                else -> false
//            }
//        }
//        popupMenu.show()
//    }
//
//    fun displayImage(imageRef: StorageReference) {
//        imageRef.downloadUrl.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val imageUrl = task.result.toString()
//                val imageView: ImageView = findViewById(R.id.imageView)
//                ImageLoader.loadImage(this, imageUrl, imageView)
//            } else {
//                // Xử lý lỗi nếu cần
//            }
//        }
//    }
//
//    fun displayImage2(imageUrl: String) {
//        val imageView: ImageView = findViewById(R.id.imageView)
//        ImageLoader.loadImage(this, imageUrl, imageView)
//    }
//
//    fun uploadImageToFirebaseStorage(imageUri: Uri, imageName: String, onComplete: (String?) -> Unit) {
//        val storage = Firebase.storage
//        val storageRef = storage.reference
//        val imageRef: StorageReference = storageRef.child("images/$imageName")
//
//        imageRef.putFile(imageUri)
//            .addOnSuccessListener {
//                // Upload success
//                imageRef.downloadUrl.addOnSuccessListener { uri ->
//                    onComplete(uri.toString())
//                }
//            }
//            .addOnFailureListener {
//                // Upload failed
//                onComplete(null)
//            }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            // Uri của ảnh được chọn
//            val selectedImageUri: Uri? = data?.data
//
//            // Gọi hàm uploadImageToFirebaseStorage với Uri này
//            selectedImageUri?.let {
//                val imageName = "user123.png" // Tên bạn muốn đặt cho ảnh trên Firebase Storage
//
//                uploadImageToFirebaseStorage(it, imageName) { imageUrl ->
//                    if (imageUrl != null) {
//                        Toast.makeText(this, "thành công", Toast.LENGTH_SHORT).show()
//                        displayImage2(imageUrl);
//                    } else {
//                        // Upload thất bại, xử lý lỗi nếu cần
//                    }
//                }
//            }
//        }
//    }
    }
}