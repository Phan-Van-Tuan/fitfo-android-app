package com.example.ff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class postAdapter(var dsPost:List<OutDataPost>): RecyclerView.Adapter<postAdapter.DsPost>() {
    inner class DsPost(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DsPost {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_baiviet,parent,false)
        return DsPost(view)
    }

    override fun onBindViewHolder(holder: DsPost, position: Int) {
        holder.itemView.apply {
            var imgAvtPost = findViewById<ImageView>(R.id.imgAvtPost)
            var txtNameBai = findViewById<TextView>(R.id.txtNameBai)
            var txtLydo = findViewById<TextView>(R.id.txtLydo)
            var txtThoigian = findViewById<TextView>(R.id.txtThoigian)
            var txtStatus = findViewById<TextView>(R.id.txtStatus)
            var picture = findViewById<ImageView>(R.id.picture)
            var count_heart = findViewById<TextView>(R.id.count_heart)
            var countCmt = findViewById<TextView>(R.id.countCmt)
            imgAvtPost.setImageResource(dsPost[position].imgAvtPost)
            picture.setImageResource(dsPost[position].picture)
            txtNameBai.setText(dsPost[position].txtNameBai)
            txtLydo.setText(dsPost[position].txtLydo)
            txtThoigian.setText(dsPost[position].txtThoigian)
            txtStatus.setText(dsPost[position].txtStatus)
            count_heart.setText(dsPost[position].count_heart)
            countCmt.setText(dsPost[position].countCmt)
        }
    }

    override fun getItemCount(): Int {
        return dsPost.size
    }
}