package com.example.ff

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.Adapter.ListMessageAdapter
import com.example.ff.Interface.ApiService
import com.example.ff.Models.findChatResponse
import com.example.ff.OutData.OutDataMessage
import com.example.ff.Test.NameChat
import com.example.ff.databinding.ActivityChatBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userId = NameChat.userID
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID",null).toString()
        fetchChat(myId,userId);

//        Toast.makeText(this, myId, Toast.LENGTH_LONG).show()
//        Toast.makeText(this, userId, Toast.LENGTH_LONG).show()


        pickImagesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        val intent = intent
        val item = NameChat.userName
        binding.txtNameChat.setText(item)
        val listmessage = mutableListOf<OutDataMessage>()
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","25/11"))
//        listmessage.add(OutDataMessage(1,R.drawable.k,"hi ","26/11"))
//        listmessage.add(OutDataMessage(2,R.drawable.k,"bạn khỏe không","26/11"))
//        listmessage.add(OutDataMessage(2,R.drawable.k,"hi ","27/11"))
//        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
//        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","25/11"))
//        listmessage.add(OutDataMessage(2,R.drawable.k,"hi chào cậu","26/11"))
//        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","22/11"))
//        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
//        listmessage.add(OutDataMessage(2,R.drawable.k,"hi chào cậu","24/11"))
//        listmessage.add(OutDataMessage(1,R.drawable.k,userId,"26/11"))
//        listmessage.add(OutDataMessage(2,R.drawable.k,,"27/11"))
        val adapterListmessage = ListMessageAdapter(listmessage)
        var lmss = binding.chatRecyclerView
        lmss.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        lmss.adapter = adapterListmessage
        lmss.scrollToPosition(adapterListmessage.itemCount - 1)
        lmss.setHasFixedSize(true)
        binding.txtNameChat.setText(item)

        binding.btnSent.setOnClickListener {
            listmessage.add(OutDataMessage(1, R.drawable.k, binding.edtMessage.text.toString(), "Ngày gửi"))

            // Thông báo cho Adapter là dữ liệu đã thay đổi
            adapterListmessage.notifyDataSetChanged()

            // Cuộn RecyclerView đến cuối cùng để hiển thị tin nhắn mới nhất
            lmss.scrollToPosition(adapterListmessage.itemCount - 1)

            // Xóa nội dung trong EditText sau khi gửi tin nhắn
            binding.edtMessage.text.clear()
        }

        binding.edtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Xử lý trước khi văn bản thay đổi
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Xử lý khi văn bản thay đổi
                if ( count > 0) {
                    binding.btnMic.visibility= View.GONE
                    binding.btnImageSent.visibility= View.GONE
                    binding.btnOptionChat.visibility= View.GONE
                    binding.btnSent.visibility= View.VISIBLE
                }else{
                    binding.btnMic.visibility= View.VISIBLE
                    binding.btnImageSent.visibility= View.VISIBLE
                    binding.btnOptionChat.visibility= View.VISIBLE
                    binding.btnSent.visibility= View.GONE
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

    private fun fetchChat(myId: String, userId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fitfo-api.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.findChat(myId, userId)
        call.enqueue(object : Callback<List<findChatResponse>> {
            override fun onResponse(
                call: Call<List<findChatResponse>>,
                response: Response<List<findChatResponse>>
            ) {
                if (response.isSuccessful) {
                    val chatResponses = response.body()

                    if (!chatResponses.isNullOrEmpty()) {
                        val firstChatResponse = chatResponses[0]
                        val chatId = firstChatResponse._id
                        val chatMember = firstChatResponse.member

                        Toast.makeText(this@ChatActivity, "phản hồi thành công $chatId", Toast.LENGTH_SHORT).show()

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(this@ChatActivity, "Không có chat nào", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ChatActivity, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<findChatResponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@ChatActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }






}