package com.example.fitfo.Define

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.example.test_firebase.ImageLoader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

object ImageUtils {

    const val PICK_IMAGE_REQUEST = 123

    fun handleImagePick(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun displayImage(imageRef: StorageReference, imageView: ImageView) {
        imageRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUrl = task.result.toString()
                ImageLoader.loadImage(imageView.context, imageUrl, imageView)
            } else {
                // Handle error if needed
            }
        }
    }

    fun displayImage2(imageUrl: String, imageView: ImageView) {
        ImageLoader.loadImage(imageView.context, imageUrl, imageView)
    }

    fun uploadImageToFirebaseStorage(
        activity: Activity,
        imageUri: Uri,
        imageName: String,
        onComplete: (String?) -> Unit
    ) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef: StorageReference = storageRef.child("$imageName")

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

    fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Uri of the selected image
            val selectedImageUri: Uri? = data?.data

            // Call uploadImageToFirebaseStorage function with this Uri
            if (selectedImageUri != null) {
                val imageName = "user123.png" // Đặt tên mong muốn cho hình ảnh trên Firebase Storage

                uploadImageToFirebaseStorage(activity, selectedImageUri, imageName) { imageUrl ->
                    if (imageUrl != null) {
                        Toast.makeText(activity, "Thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        // Xử lý lỗi nếu cần thiết
                    }
                }
            }
        }
    }
}
