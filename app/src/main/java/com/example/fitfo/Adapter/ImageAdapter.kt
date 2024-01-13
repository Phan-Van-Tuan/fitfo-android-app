package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.R

class ImageAdapter(var listimage:List<String>, val rvInterfaceChat: RvChat):RecyclerView.Adapter<ImageAdapter.listImage> (){
    inner class listImage(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listImage {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image,parent,false)
        return listImage(view)
    }

    override fun onBindViewHolder(holder: listImage, position: Int) {
        holder.itemView.apply {
            var image = findViewById<ImageView>(R.id.image)
            val imageUrl = listimage[position]
            if (!imageUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(imageUrl, image)
            }
        }
    }



    override fun getItemCount(): Int {
        return listimage.size
    }
}