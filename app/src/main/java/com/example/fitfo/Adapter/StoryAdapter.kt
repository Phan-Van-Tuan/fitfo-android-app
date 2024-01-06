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

class StoryAdapter( var liststory:List<GetStoryReponse>, val rvInterfaceChat: RvChat):RecyclerView.Adapter<StoryAdapter.listStory> (){
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
            val avatarUrl = liststory[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
            ImageUtils.displayImage2(avatarUrl, imgAvtStory)
        }
//            imgStory.setImageResource(liststory[position].photo)
            txtNameStory.setText(liststory[position].userName)
            holder.itemView.setOnClickListener {
                rvInterfaceChat.onClickchat(position)
            }
//            val retrofit = Retrofit.Builder()
//                .baseUrl("http://192.168.1.175:3200/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            val apiService = retrofit.create(ApiService::class.java)
//            val call = apiService.deletePost(postId)
//
//            call.enqueue(object : Callback<String> {
//                override fun onResponse(
//                    call: Call<String>,
//                    response: retrofit2.Response<String>
//                ) {
//                    Toast.makeText(context, response.body(), Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    // Handle failure
//                    Log.e("API Call", "Failed to delete story", t)
//                    Toast.makeText(context, "Failed to delete story. Please try again.1", Toast.LENGTH_SHORT).show()
//                }
//            })
        }
    }



    override fun getItemCount(): Int {
        return liststory.size
    }
}