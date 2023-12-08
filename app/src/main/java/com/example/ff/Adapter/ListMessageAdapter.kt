package com.example.ff.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.OutData.OutDataMessage
import com.example.ff.R

class ListMessageAdapter(var listmessage: MutableList<OutDataMessage>):RecyclerView.Adapter<ListMessageAdapter.listMessage>(){
    inner class listMessage(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listMessage {
        if (viewType==2){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container_received_message,parent,false)
            return listMessage(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container_sent_message,parent,false)
            return listMessage(view)
        }

    }


    override fun onBindViewHolder(holder: listMessage, position: Int) {
        holder.itemView.apply {
            val txtMessage = findViewById<TextView>(R.id.txtMessage)
            val txtDateTime = findViewById<TextView>(R.id.txtDateTime)
            val imgProfile = findViewById<ImageView>(R.id.imgProfile)
            txtMessage.setText(listmessage[position].txtMessage)
            txtDateTime.setText(listmessage[position].txtDateTime)
            imgProfile.setImageResource(listmessage[position].imgProfile)
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (listmessage[position].id==1){
            return 1
        }else{
            return 2
        }
    }


    override fun getItemCount(): Int {
        return listmessage.size
    }
}