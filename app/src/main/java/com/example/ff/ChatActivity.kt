package com.example.ff

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ff.Adapter.ListMessageAdapter
import com.example.ff.Interface.ApiService
import com.example.ff.Models.findChatResponse
import com.example.ff.Models.findMessageResponse
import com.example.ff.Test.NameChat
import com.example.ff.databinding.ActivityChatBinding
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImages: ArrayList<String> = ArrayList()
    private lateinit var pickImagesLauncher: ActivityResultLauncher<Intent>
    private var listmessages: MutableList<findMessageResponse> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()

        val chatId = NameChat.chatID
        if(chatId != "") {
            fectchMessage(chatId, myId)
        }

//        binding.btnSent.setOnClickListener {
//            listmessages.add(findMessageResponse(1, R.drawable.k, binding.edtMessage.text.toString(), "Ngày gửi"))
//
//            // Thông báo cho Adapter là dữ liệu đã thay đổi
//            adapterListmessage.notifyDataSetChanged()
//
//            // Cuộn RecyclerView đến cuối cùng để hiển thị tin nhắn mới nhất
//            lmss.scrollToPosition(adapterListmessage.itemCount - 1)
//
//            // Xóa nội dung trong EditText sau khi gửi tin nhắn
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
        val item = NameChat.userName
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
                        lmss.layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
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
                        Toast.makeText(this@ChatActivity, "im here", Toast.LENGTH_SHORT)
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


}