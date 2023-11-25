package com.example.ff.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.OutData.OutDataPost
import com.example.ff.R

class postAdapter(var listPost:List<OutDataPost>): RecyclerView.Adapter<postAdapter.ListPost>() {
    inner class ListPost(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPost {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_port,parent,false)
        return ListPost(view)
    }

    override fun onBindViewHolder(holder: ListPost, position: Int) {
        holder.itemView.apply {
            var imgAvtPost = findViewById<ImageView>(R.id.imgAvtPost)
            var txtNamePost = findViewById<TextView>(R.id.txtNamePort)
            var txtCause = findViewById<TextView>(R.id.txtCause)
            var txtTimePost = findViewById<TextView>(R.id.txtTimeport)
            var txtStatus = findViewById<TextView>(R.id.txtStatus)
            var picturePost = findViewById<ImageView>(R.id.picturePort)
            var count_heart = findViewById<TextView>(R.id.count_heart)
            var countCmt = findViewById<TextView>(R.id.countCmt)
            imgAvtPost.setImageResource(listPost[position].imgAvtPost)
            picturePost.setImageResource(listPost[position].picturePort)
            txtNamePost.setText(listPost[position].txtNamePort)
            txtCause.setText(listPost[position].txtCause)
            txtTimePost.setText(listPost[position].txtTimeport)
            txtStatus.setText(listPost[position].txtStatus)
            count_heart.setText(listPost[position].count_heart)
            countCmt.setText(listPost[position].countCmt)
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}