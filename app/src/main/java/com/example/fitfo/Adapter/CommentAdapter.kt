package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.findCommentResponse
import com.example.fitfo.R


class CommentAdapter(var Comment:List<findCommentResponse>, val rvInterfaceChat: RvChat): RecyclerView.Adapter<CommentAdapter.listcomment>() {
inner class listcomment(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listcomment {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_comment,parent,false)
        return listcomment(view)
    }

    override fun onBindViewHolder(holder:listcomment, position: Int) {
        holder.itemView.apply {
            var txtNameCmt = findViewById<TextView>(R.id.txtNameCmt)
            var txtTitleCmt = findViewById<TextView>(R.id.txtTitleCmt)
            var imgAvtCmt = findViewById<ImageView>(R.id.imgAvtCmt)
            txtNameCmt.setText(Comment[position].userName)
//            imgAvtCmt.setImageResource(Comment[position].avatar)
            txtTitleCmt.setText(Comment[position].content)
            holder.itemView.setOnClickListener {
                rvInterfaceChat.onClickchat(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return Comment.size
    }
}