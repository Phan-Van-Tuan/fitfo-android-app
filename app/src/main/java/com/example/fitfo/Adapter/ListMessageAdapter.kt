package com.example.fitfo.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Models.findMessageResponse
import com.example.fitfo.R
import com.example.fitfo.Define.DateFormat
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.UserInfo


class ListMessageAdapter(
    var listmessage: MutableList<findMessageResponse>, private
    var myId: String
) : RecyclerView.Adapter<ListMessageAdapter.listMessage>() {
    val dateFormat = DateFormat();
    inner class listMessage(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listMessage {

        if (viewType == 2) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_received_message, parent, false)
            return listMessage(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_sent_message, parent, false)
            return listMessage(view)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: listMessage, position: Int) {
        holder.itemView.apply {
            val txtMessage = findViewById<TextView>(R.id.txtMessage)
            val txtDateTime = findViewById<TextView>(R.id.txtDateTime)
            val imgProfile = findViewById<ImageView>(R.id.imgProfile)
            val userAvatar = UserInfo.userAvatar
            if (!userAvatar.isNullOrEmpty()) {
                ImageUtils.displayImage2(userAvatar, imgProfile)
            }
            txtMessage.setText(listmessage[position].title)
            var dateTimeFormated = dateFormat.toFormattedString(listmessage[position].createdAt)
            txtDateTime.setText(dateTimeFormated)
//            imgProfile.setImageDrawable()
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (listmessage[position].senderId == myId) {
            return 1
        } else {
            return 2
        }
    }


    override fun getItemCount(): Int {
        return listmessage.size
    }
}