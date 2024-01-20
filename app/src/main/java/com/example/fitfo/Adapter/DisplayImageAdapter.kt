package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.R

class DisplayImageAdapter(var listimage:List<String>):RecyclerView.Adapter<DisplayImageAdapter.listImage> (){
    inner class listImage(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listImage {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_full_screen,parent,false)
        return listImage(view)
    }

    override fun onBindViewHolder(holder: listImage, position: Int) {
        holder.itemView.apply {
            var image = findViewById<ImageView>(R.id.image_full_screen)
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