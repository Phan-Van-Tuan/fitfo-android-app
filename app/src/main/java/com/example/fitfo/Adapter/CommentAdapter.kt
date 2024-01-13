package com.example.fitfo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.DateFormat
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.findCommentResponse
import com.example.fitfo.R
import org.threeten.bp.Instant


class CommentAdapter(var Comment:List<findCommentResponse>, val rvInterfaceChat: RvChat)
    : RecyclerView.Adapter<CommentAdapter.listcomment>() {
    val dateFormat = DateFormat();
inner class listcomment(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listcomment {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_comment,parent,false)
        return listcomment(view)
    }

    override fun onBindViewHolder(holder:listcomment, position: Int) {
        holder.itemView.apply {
            // anh xa
            var userName = findViewById<TextView>(R.id.comment_user_name)
            var content = findViewById<TextView>(R.id.comment_content)
            var avatar = findViewById<ImageView>(R.id.comment_user_name_avatar)
            var time = findViewById<TextView>(R.id.comment_time)

            // truyen du lieu
            userName.setText(Comment[position].userName)
            content.setText(Comment[position].content)
            val avatarUrl = Comment[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(avatarUrl, avatar)
            }
            val dateTimeString = Comment[position].createdAt ?: ""
            if (!dateTimeString.isNullOrEmpty()) {
                val dateTimeFormated = dateFormat.toFormattedString(dateTimeString)
                time.setText(dateTimeFormated)
            }

            // handle click comment item
            holder.itemView.setOnClickListener {
                rvInterfaceChat.onClickchat(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return Comment.size
    }
}