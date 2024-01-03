package com.example.fitfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.ListMessageAdapter
import com.example.fitfo.Interface.ApiService
import com.example.fitfo.Models.findMessageResponse
import com.example.fitfo.Socket.MySocketManager
import com.example.fitfo.Test.MyInfo
import com.example.fitfo.databinding.ActivityChatBinding
import com.jakewharton.threetenabp.AndroidThreeTen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImages: ArrayList<String> = ArrayList()
    private lateinit var pickImagesLauncher: ActivityResultLauncher<Intent>
    private var listmessages: MutableList<findMessageResponse> = mutableListOf()
    private val SPEECH_REQUEST_CODE = 0
    private val socketManager = MySocketManager()

    private val speechRecognitionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onSpeechRecognitionResult(result.resultCode, result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(binding.root)
        binding.btnMic.setOnClickListener {
            displaySpeechRecognizer()
        }

        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()

        val chatId = MyInfo.chatID

        if(chatId != "") {
            fectchMessage(chatId, myId)
        } else return

        socketManager.initSocket();
        socketManager.connectSocket();
        socketManager.addNewUser(myId);

        binding.btnSent.setOnClickListener {
            val myMessage = binding.edtMessage.text.toString().trim();
            socketManager.sendMessage("""{"chatId":"$chatId","senderId":"$myId","title":"$myMessage"}""");

            // Xóa nội dung trong EditText sau khi gửi tin nhắn
            binding.edtMessage.text.clear()
            fectchMessage(chatId, myId);
        }

//        binding.btnSent.setOnClickListener {
//            val myMessage = binding.edtMessage.text.toString().trim()
//            socketManager.sendMessage("{'chatId': '$chatId','senderId': '$myId','title':$myMessage}")
////            Toast.makeText(this,myMessage,Toast.LENGTH_LONG).show()
//            socketManager.onReceiveMessage { args ->
//                // Xử lý khi nhận được tin nhắn từ server
//                val receiveMessage = args[0] as String
//                Toast.makeText(this, receiveMessage, Toast.LENGTH_LONG).show()
//            }
//            binding.edtMessage.text.clear()
//        }


        pickImagesLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // Lấy danh sách các Uri của hình ảnh đã chọn
                    result.data?.let { intent ->
                        val clipData = intent.clipData
                        if (clipData != null) {
                            // Nếu người dùng chọn nhiều hình ảnh
                            for (i in 0 until clipData.itemCount) {
                                val uri = clipData.getItemAt(i).uri
                                selectedImages.add(uri.toString())
                            }
                        } else {
                            // Nếu người dùng chỉ chọn một hình ảnh
                            val uri = intent.data
                            uri?.let {
                                selectedImages.add(it.toString())
                            }
                        }

                        // Hiển thị hình ảnh hoặc thực hiện các thao tác khác với danh sách hình ảnh đã chọn
                        // ...
                    }
                }
            }

        binding.btnImageSent.setOnClickListener {
            pickMultipleImages()
        }

//        val intent = intent

        // Kiểm tra xem Intent có dữ liệu không

        // Kiểm tra xem Intent có dữ liệu không
//        if (intent.hasExtra("idChat")) {
//            // Lấy dữ liệu từ Intent
//            val chatId = intent.getStringExtra("idChat")
//
//            // Gọi hàm hoặc thực hiện các công việc khác với dữ liệu nhận được
//            if (chatId != null) {
//                fectchMessage(chatId,myId)
//            };
//        }
        val item = MyInfo.userName
        binding.txtNameChat.setText(item)


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
                if (count > 0) {
                    binding.btnMic.visibility = View.GONE
                    binding.btnImageSent.visibility = View.GONE
                    binding.btnOptionChat.visibility = View.GONE
                    binding.btnSent.visibility = View.VISIBLE
                } else {
                    binding.btnMic.visibility = View.VISIBLE
                    binding.btnImageSent.visibility = View.VISIBLE
                    binding.btnOptionChat.visibility = View.VISIBLE
                    binding.btnSent.visibility = View.GONE
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // Xử lý sau khi văn bản đã thay đổi
            }
        })


        binding.chatBack.setOnClickListener {
            var intent = Intent(this, MainActivity_Logged_in::class.java)
            startActivity(intent)
        }


    }

    private fun pickMultipleImages() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        // Mở trình quản lý hình ảnh để chọn nhiều hình ảnh
        pickImagesLauncher.launch(intent)
    }


    private fun fectchMessage(chatId: String, myId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fitfo-api.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.findMessages(chatId)
        call.enqueue(object : Callback<List<findMessageResponse>> {
            override fun onResponse(
                call: Call<List<findMessageResponse>>,
                response: Response<List<findMessageResponse>>
            ) {
                if (response.isSuccessful) {
                    val messageResponses = response.body()

                    if (!messageResponses.isNullOrEmpty()) {
                        listmessages.addAll(messageResponses);
//                        val firstChatResponse = chatResponses[0]
//                        val messageId = firstChatResponse._id;
//                        val chatId = firstChatResponse.chatId;
//                        val senderId = firstChatResponse.senderId;
//                        val text = firstChatResponse.text;
//                        val createdAt = firstChatResponse.createdAt;
                        val adapterListmessage = ListMessageAdapter(listmessages, myId)
                        var lmss = binding.chatRecyclerView
                        lmss.layoutManager = LinearLayoutManager(
                            this@ChatActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        lmss.adapter = adapterListmessage
                        lmss.scrollToPosition(adapterListmessage.itemCount - 1)
                        lmss.setHasFixedSize(true)
//                        Toast.makeText(
//                            this@ChatActivity,
//                            "phản hồi thành công \n" +
//                                    myId, Toast.LENGTH_SHORT
//                        ).show()

                        // TODO: Thực hiện xử lý với thông tin người dùng
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

    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        // Launch the speech recognition activity
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val matches: ArrayList<String>? = data?.getStringArrayListExtra(
            RecognizerIntent.EXTRA_RESULTS
        )
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            var spokentText = speechResults?.get(0)
            Log.d("hhhh", spokentText.toString())
        }

    }

    private fun onSpeechRecognitionResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val spokenText: String? =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            // Do something with spokenText.
            binding.edtMessage.setText(spokenText)
        }
    }
}