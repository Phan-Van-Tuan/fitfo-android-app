package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.OutData.OutDataContact
import com.example.fitfo.R


class ContactAdapter(var Contact: MutableList<OutDataContact>): RecyclerView.Adapter<ContactAdapter.listcontact>() {
inner class listcontact(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listcontact {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_contact,parent,false)
        return listcontact(view)
    }

    override fun onBindViewHolder(holder: listcontact, position: Int) {
        holder.itemView.apply {
            var txtNameChat = findViewById<TextView>(R.id.txtNameChat)
            var imgAvtChat = findViewById<ImageView>(R.id.imgAvtChat)
            txtNameChat.setText(Contact[position].txtNameChat)
            imgAvtChat.setImageResource(Contact[position].imgAvtChat)
        }
    }

    override fun getItemCount(): Int {
        return Contact.size
    }
}