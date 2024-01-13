package com.example.fitfo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Adapter.ListMessageAdapter
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.MySocketManager
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Models.findMessageResponse
import com.example.fitfo.Profile.ProfileActivity
import com.example.fitfo.databinding.ActivityChatBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.jakewharton.threetenabp.AndroidThreeTen
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapterListMessage: ListMessageAdapter
    private var listmessages: MutableList<findMessageResponse> = mutableListOf()
    private val socketManager = MySocketManager()
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // lấy ra dữ liệu cần thiết
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()
        val chatId = UserInfo.chatID
        val userName = UserInfo.userName
        binding.txtNameChat.setText(userName)

//        // khởi tạo socket

        socketManager.initSocket()
        socketManager.connectSocket()
        socketManager.addNewUser(myId)

        // Khởi tạo adapter một lần duy nhất trong onCreate()
        adapterListMessage = ListMessageAdapter(listmessages, myId)
        var listMessagesRecyclerView = binding.chatRecyclerView
        listMessagesRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        listMessagesRecyclerView.setHasFixedSize(true)

        // khởi tạo ActivityResultLauncher
        var imagePickResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedImages: List<Uri> = ImageUtils.handleImagePickerResult(result.data)
                    // Xử lý danh sách các Uri của ảnh đã chọn ở đây
                    val selectedImages1 = mutableListOf<Uri>()
                    selectedImages1.addAll(selectedImages)
                    // Đặt hàm uploadImagesToFirebaseStorage() bên trong callback
                    ImageUtils.uploadImagesToFirebaseStorage(
                        this,
                        selectedImages1,
                        "messages"
                    ) { uploadedImageUrls ->
                        // Xử lý danh sách các đường dẫn của ảnh đã tải lên ở đây
                        // Nối các url lại với nhau thành một chuỗi String
                        var myPhotoMessage = uploadedImageUrls.joinToString(",")
                        socketManager.sendMessage("""{"chatId":"$chatId","senderId":"$myId","title":"$myPhotoMessage","type":"photo"}""");
                    }
                }
            }

        // khởi tạo để dateFormat
        AndroidThreeTen.init(this)

        // nhấn vô tên thì chuyển qua profile
        binding.txtNameChat.setOnClickListener {
            if (socketManager != null) {
                socketManager.disconnectSocket()
            }
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // socket lắng nghe
        socketManager.onReceiveMessage { args ->
            // Xử lý thông báo ở đây
            if (args.isNotEmpty()) {
                val messageData = args[0] as JSONObject
                // Chuyển đổi JSON thành đối tượng Kotlin
                val findMessageResponse = Gson().fromJson(messageData.toString(), findMessageResponse::class.java)
                Log.d("onReceiveMessage.......", "$findMessageResponse")

                // Ví dụ: Hiển thị thông báo, cập nhật giao diện người dùng, v.v.
                runOnUiThread {
                    // Thêm đối tượng vào danh sách messages
                    listmessages.add(0, findMessageResponse)
                    // Cập nhật RecyclerView
                    adapterListMessage.notifyDataSetChanged()
                    // Cuộn ListView đến vị trí cuối cùng
                    binding.chatRecyclerView.scrollToPosition(0)
                }
            }
        }

        // sử kiện nhấn vào nút gửi tin nhắn
        binding.btnSent.setOnClickListener {
            val myMessage = binding.edtMessage.text.toString().trim();
            socketManager.sendMessage("""{"chatId":"$chatId","senderId":"$myId","title":"$myMessage","type":"message"}""");
            binding.edtMessage.text.clear()
        }

        // xử lý khi nhấn vào nút pick ảnh
        binding.btnImageSent.setOnClickListener {
            // Sử dụng ActivityResultLauncher đã đăng ký để khởi chạy hàm pickImages()
            ImageUtils.pickImages(this, imagePickResultLauncher)
        }

        // xử lý khi nhấn vào nút mic
        binding.btnMic.setOnClickListener {
            displayMicro {
                if (it.isNotEmpty()) {
                    // Gửi message qua socket
                    socketManager.sendMessage("""{"chatId":"$chatId","senderId":"$myId","title":"$it","type":"audio"}""")
                }
            }
        }

        // xử lý khi nhập text vào ô input message
        binding.edtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Xử lý trước khi văn bản thay đổi
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Xử lý khi văn bản thay đổi
                if (binding.edtMessage.text.toString().isNotEmpty()) {
                    binding.btnMic.visibility = View.GONE
                    binding.btnImageSent.visibility = View.GONE
//                    binding.btnOptionChat.visibility = View.GONE
                    binding.btnSent.visibility = View.VISIBLE
                } else {
                    binding.btnMic.visibility = View.VISIBLE
                    binding.btnImageSent.visibility = View.VISIBLE
//                    binding.btnOptionChat.visibility = View.VISIBLE
                    binding.btnSent.visibility = View.GONE
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // Xử lý sau khi văn bản đã thay đổi
            }
        })

        // xử lý khi nhấn nuts back
        binding.chatBack.setOnClickListener {
            onBackPressed()
        }

        if(chatId != "") {
            fetchMessage(chatId, listMessagesRecyclerView)
        } else return
    }

    private fun fetchMessage(chatId: String, listMessagesRecyclerView: RecyclerView) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findMessages(chatId)
        call.enqueue(object : Callback<List<findMessageResponse>> {
            override fun onResponse(
                call: Call<List<findMessageResponse>>,
                response: Response<List<findMessageResponse>>
            ) {
                if (response.isSuccessful) {
                    val messageResponses = response.body()

                    if (!messageResponses.isNullOrEmpty()) {
                        // Thêm dữ liệu vào listmessages
                        listmessages.addAll(messageResponses);
                        // Cập nhật ListView bằng cách gọi notifyDataSetChanged()
                        adapterListMessage.notifyDataSetChanged()
                        listMessagesRecyclerView.adapter = adapterListMessage
                        // Cuộn ListView đến vị trí cuối cùng
                        binding.chatRecyclerView.scrollToPosition(0)
                    } else {
                        Toast.makeText(this@ChatActivity, "Bạn chưa nhắn tin với người này, hãy bắt đầu trò chuyện", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ChatActivity, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<findMessageResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@ChatActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun startRecording() {
        // xin quyền cái mic
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        } else {
            try {
                mediaRecorder = MediaRecorder()
                audioFile = File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "audio_file.mp3")

                mediaRecorder?.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(audioFile?.absolutePath)
                    prepare()
                    start()
                }
            } catch (e: IOException) {
                Toast.makeText(this, "Error during recording: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun stopRecording(returnUrl: (String) -> Unit = {}) {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }

            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            val inputStream = FileInputStream(audioFile)
            var bytesRead: Int

            bytesRead = inputStream.read(buffer)
            while (bytesRead != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }

            val audioData = byteArrayOutputStream.toByteArray()

            // Gọi hàm để tải lên Firebase Storage
            uploadAudioToFirebaseStorage(audioData) {
                returnUrl(it)
            }

        } catch (e: Exception) {
            // Xử lý ngoại lệ
            Toast.makeText(this, "Error during recording: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    private fun uploadAudioToFirebaseStorage(audioData: ByteArray, returnUrl: (String) -> Unit = {}) {
        val currentTimeMillis = System.currentTimeMillis()
        val storageReference: StorageReference =
            storageRef.child("audio_files/$currentTimeMillis.mp3")

        val uploadTask = storageReference.putStream(ByteArrayInputStream(audioData))
        uploadTask.addOnSuccessListener {
            // Lấy URL của tệp âm thanh
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                // Ở đây bạn có thể lưu audioUrl vào cơ sở dữ liệu của bạn.
                returnUrl.invoke(uri.toString())
            }
        }.addOnFailureListener { e ->
            // Handle unsuccessful uploads
            Toast.makeText(this, "Error uploading file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayMicro(returnUrl: (String) -> Unit = {}) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_diplay_micro, null)
        val close = view.findViewById<ImageView>(R.id.button_close_micro)
//        val send = view.findViewById<ImageView>(R.id.button_send_record)
        val record = view.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.button_record)

        close.setOnClickListener {
            dialog.dismiss()
        }

        record.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Áp dụng animation khi nút được giữ
                    record.playAnimation()

                    // Thực hiện các hành động khi nút được giữ
                    startRecording()

                    true // Trả về true để chỉ định rằng sự kiện đã được xử lý
                }
                MotionEvent.ACTION_UP -> {
                    // Thực hiện các hành động khi nút được thả ra
                    stopRecording {
                        returnUrl(it)
                    }
                    record.pauseAnimation()
                    record.progress = 0F

                    false // Trả về false để chỉ định rằng sự kiện đã được xử lý
                }
                else -> false
            }
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)

        dialog.show()
    }

    // Xử lý kết quả yêu cầu quyền
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecording()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ngắt kết nối socket khi activity bị hủy
        if (socketManager != null) {
            socketManager.disconnectSocket()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (socketManager != null) {
            socketManager.disconnectSocket()
        }
        val intent = Intent(this, Logged::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}