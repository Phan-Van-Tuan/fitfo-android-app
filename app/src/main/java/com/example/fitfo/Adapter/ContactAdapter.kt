package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.getListFriendResponse
import com.example.fitfo.R


class ContactAdapter(var Contact: MutableList<getListFriendResponse>, val rvChat: RvChat): RecyclerView.Adapter<ContactAdapter.listcontact>() {
inner class listcontact(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listcontact {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_contact,parent,false)
        return listcontact(view)
    }

    override fun onBindViewHolder(holder: listcontact, position: Int) {
        holder.itemView.apply {
            var nameContact = findViewById<TextView>(R.id.txtNameChat)
            var avatarContact = findViewById<ImageView>(R.id.imgAvtChat)
            nameContact.setText(Contact[position].name)
            val avatarUrl = Contact[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(avatarUrl, avatarContact)
            }
            holder.itemView.setOnClickListener {
                rvChat.onClickchat(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return Contact.size
    }
}