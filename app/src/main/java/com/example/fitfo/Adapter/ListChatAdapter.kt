package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.DateFormat
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.R
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.ChatResponse


class ListChatAdapter(private var listChat: MutableList<ChatResponse>, val myId: String, private val rvInterfaceChat: RvChat) :
    RecyclerView.Adapter<ListChatAdapter.listchat>() {
    inner class listchat(itemView: View) : RecyclerView.ViewHolder(itemView)
    private val dateFormat = DateFormat()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listchat {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listchat, parent, false)
        return listchat(view)
    }

    override fun onBindViewHolder(holder: listchat, position: Int) {
        holder.itemView.apply {
            var txtNameChat = findViewById<TextView>(R.id.txtNameChat)
            var txtTitle = findViewById<TextView>(R.id.txtTitle)
            var txtTimeOnline = findViewById<TextView>(R.id.txtTimeOnline)
            var imgAvtChat = findViewById<ImageView>(R.id.imgAvtChat)

            txtNameChat.text = listChat[position].chatName
//            txtTimeOnline.setText(listChat[position].txtTimeOnline)
            var avatarUrl = listChat[position].chatAvatar
            if (!avatarUrl.isNullOrEmpty()) {
                ImageUtils.displayImage2(avatarUrl, imgAvtChat)
            }

            if(listChat[position].latestSenderId == myId) {
                txtTitle.text = "You: ${listChat[position].latestType}"
            } else {
                val type = listChat[position].latestType
                if(type == "message") {
                    txtTitle.text = "${listChat[position].chatName}: ${listChat[position].latestMessage}"
                } else {
                    txtTitle.text = "${listChat[position].chatName}: $type"
                }
            }

            var dateTimeFormated = dateFormat.toFormattedString(listChat[position].latestSend)
            txtTimeOnline.setText(dateTimeFormated)

            holder.itemView.setOnClickListener {
                rvInterfaceChat.onClickchat(position)
            }
        }
    }


    override fun getItemCount(): Int {
        return listChat.size
    }
}