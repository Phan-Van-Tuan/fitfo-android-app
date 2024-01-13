package com.example.fitfo.Define

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.test_firebase.ImageLoader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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

    fun handleImagePickerResult(data: Intent?): List<Uri> {
        val selectedImages: MutableList<Uri> = mutableListOf()

        val clipData = data?.clipData
        val dataString = data?.data

        if (clipData != null) {
            for (i in 0 until clipData.itemCount) {
                val uri = clipData.getItemAt(i).uri
                selectedImages.add(uri)
            }
        } else if (dataString != null) {
            selectedImages.add(dataString)
        }

        return selectedImages
    }

    fun pickImages(activity: Activity, imagePickResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imagePickResultLauncher?.launch(intent)
    }

    fun uploadImagesToFirebaseStorage(
        activity: Activity,
        imageUris: List<Uri>,
        dir: String,
        onComplete: (List<String?>) -> Unit
    ) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val uploadedImageUrls: MutableList<String?> = mutableListOf()

        val uploadTasks = mutableListOf<StorageReference>()

        imageUris.forEachIndexed { index, uri ->
            val currentTimeMillis = System.currentTimeMillis()
            val imageName = "$currentTimeMillis.png"
            val imageRef: StorageReference = storageRef.child("$dir/$imageName")

            // Tải ảnh lên Firebase Storage
            imageRef.putFile(uri).addOnSuccessListener {
                // Lấy đường dẫn của ảnh đã tải lên
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Tạo một đối tượng Uri từ đường dẫn
                    val uri = Uri.parse(downloadUri.toString())
                    // Thêm đối tượng Uri vào danh sách uploadTasks
                    uploadTasks.add(storage.getReferenceFromUrl(uri.toString()))
                    // Thêm đường dẫn vào danh sách uploadedImageUrls
                    uploadedImageUrls.add(downloadUri.toString())

                    if (uploadedImageUrls.size == imageUris.size) {
                        onComplete(uploadedImageUrls)
                    }
                }
            }.addOnFailureListener {
                onComplete(emptyList())
            }
        }
    }

}
