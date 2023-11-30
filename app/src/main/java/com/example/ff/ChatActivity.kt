package com.example.ff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.txtNameChat.setText(item)
//        binding.edtMessage.
        binding.chatBack.setOnClickListener {
            var intent = Intent(this, MainActivity_Logged_in::class.java)
            startActivity(intent)
        }

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
        lmss.setHasFixedSize(true)
    }



}