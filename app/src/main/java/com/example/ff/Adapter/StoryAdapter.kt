package com.example.ff.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.Interface.RvChat
import com.example.ff.OutData.OutData_Story
import com.example.ff.R

class StoryAdapter( var liststory:List<OutData_Story>, val rvInterfaceChat: RvChat):RecyclerView.Adapter<StoryAdapter.listStory> (){
    inner class listStory(itemView: View):RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listStory {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_story,parent,false)
        return listStory(view)
    }

    override fun onBindViewHolder(holder: listStory, position: Int) {
        holder.itemView.apply {
            var imgAvtStory = findViewById<ImageView>(R.id.imgAvtStory)
            var imgStory = findViewById<ImageView>(R.id.imgStory)
            var txtNameStory = findViewById<TextView>(R.id.txtNameStory)
            imgAvtStory.setImageResource(liststory[position].imgAvtStory)
            imgStory.setImageResource(liststory[position].imgStory)
            txtNameStory.setText(liststory[position].txtNameStory)
            holder.itemView.setOnClickListener {
                rvInterfaceChat.onClickchat(position)
            }
        }
    }



    override fun getItemCount(): Int {
        return liststory.size
    }
}