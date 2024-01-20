package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RecyclerViewOnClick
import com.example.fitfo.Interface.RecyclerViewOnLongClick
import com.example.fitfo.R

class ImageAdapter(
    private var listImage:List<String>,
    val ROC: RecyclerViewOnClick,
    val ROLC: RecyclerViewOnLongClick
):RecyclerView.Adapter<ImageAdapter.ListImage> (){
    inner class ListImage(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListImage {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image,parent,false)
        return ListImage(view)
    }

    override fun onBindViewHolder(holder: ListImage, position: Int) {
        holder.itemView.apply {
            var image = findViewById<ImageView>(R.id.image)
            val imageUrl = listImage[position]
            if (!imageUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(imageUrl, image)
            }
            holder.itemView.setOnClickListener {
                ROC.onClickItem(position)
            }
            holder.itemView.setOnLongClickListener {
                ROLC.onLongClickItem(position)
                true
            }
        }
    }



    override fun getItemCount(): Int {
        return listImage.size
    }
}