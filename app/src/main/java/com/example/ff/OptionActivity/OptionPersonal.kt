package com.example.ff.OptionActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.ff.PersonalActivity
import com.example.ff.databinding.ActivityOptionPersonalBinding
import com.example.test_firebase.ImageLoader
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class OptionPersonal : AppCompatActivity() {
    private lateinit var binding: ActivityOptionPersonalBinding
    private val PICK_IMAGE_REQUEST = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBacktoPersonal.setOnClickListener {
            var intent = Intent(this, PersonalActivity::class.java)
            startActivity(intent)
        }
        val storage = Firebase.storage("gs://fitfo-storage.appspot.com")

        // handle pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)

        binding.txtEditAvt.setOnClickListener {
            // You can add your logic here
        }
    }

    fun displayImage(imageRef: StorageReference) {
        imageRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUrl = task.result.toString()
                ImageLoader.loadImage(this, imageUrl,binding.imgpick)
            } else {
                // Handle error if needed
            }
        }
    }

    fun displayImage2(imageUrl: String) {
        ImageLoader.loadImage(this, imageUrl, binding.imgpick)
    }

    fun uploadImageToFirebaseStorage(imageUri: Uri, imageName: String, onComplete: (String?) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef: StorageReference = storageRef.child("images/$imageName")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Upload success
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onComplete(uri.toString())
                }
            }
            .addOnFailureListener {
                // Upload failed
                onComplete(null)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            // Uri of the selected image
            val selectedImageUri: Uri? = data?.data

            // Call uploadImageToFirebaseStorage function with this Uri
            if (selectedImageUri != null) {
                val imageName = "user123.png" // Đặt tên mong muốn cho hình ảnh trên Firebase Storage

                uploadImageToFirebaseStorage(selectedImageUri, imageName) { imageUrl ->
                    if (imageUrl != null) {
                        Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show()
                        displayImage2(imageUrl)
                    } else {
                        // Xử lý lỗi nếu cần thiết
                    }
                }
            }
        }
    }
}