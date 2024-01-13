package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.R
import com.example.fitfo.R.id.name_user_of_story

class StoryAdapter( var liststory:List<GetStoryReponse>, val rvInterfaceChat: RvChat):RecyclerView.Adapter<StoryAdapter.listStory> (){
    inner class listStory(itemView: View):RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listStory {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_story,parent,false)
        return listStory(view)
    }

    override fun onBindViewHolder(holder: listStory, position: Int) {
        holder.itemView.apply {
            var avatarStory = findViewById<ImageView>(R.id.avatar_user_of_story)
            var imageStory = findViewById<ImageView>(R.id.image_of_story)
            var userName = findViewById<TextView>(R.id.name_user_of_story)
            val avatarUrl = liststory[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(avatarUrl, avatarStory)
            }
            val imageUrl = liststory[position].photo
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(imageUrl, imageStory)
            }
            userName.setText(liststory[position].userName)
            holder.itemView.setOnClickListener {
                rvInterfaceChat.onClickchat(position)
            }
        }
    }



    override fun getItemCount(): Int {
        return liststory.size
    }
}