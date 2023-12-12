package com.example.ff.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.Models.findMessageResponse
import com.example.ff.R
import com.google.gson.Gson


class ListMessageAdapter(
    var listmessage: MutableList<findMessageResponse>, private
    var myId: String
) : RecyclerView.Adapter<ListMessageAdapter.listMessage>() {

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


    override fun onBindViewHolder(holder: listMessage, position: Int) {
        holder.itemView.apply {
            val txtMessage = findViewById<TextView>(R.id.txtMessage)
            val txtDateTime = findViewById<TextView>(R.id.txtDateTime)
            val imgProfile = findViewById<ImageView>(R.id.imgProfile)
            txtMessage.setText(listmessage[position].text)
            txtDateTime.setText(listmessage[position].createdAt)
//            imgProfile.setImageDrawable()
        }
    }


    override fun getItemViewType(position: Int): Int {
        // A MUỐN LẤY RA Ở ĐÂY ĐEER SO SQANHS
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