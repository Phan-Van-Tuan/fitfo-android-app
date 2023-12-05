package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.Adapter.ListMessageAdapter
import com.example.ff.OutData.OutDataMessage
import com.example.ff.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val intent = intent
        val item = intent.getStringExtra("txtNameChat")
        val listmessage = mutableListOf<OutDataMessage>()
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi ","26/11"))
        listmessage.add(OutDataMessage(2,R.drawable.k,"bạn khỏe không","26/11"))
        listmessage.add(OutDataMessage(2,R.drawable.k,"hi ","26/11"))
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(2,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(2,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(1,R.drawable.k,"hi chào cậu","26/11"))
        listmessage.add(OutDataMessage(2,R.drawable.k,"hi chào cậu","26/11"))
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





}