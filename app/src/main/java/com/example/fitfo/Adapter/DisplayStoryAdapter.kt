package com.example.fitfo.Adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.R

class DisplayStoryAdapter(var context:Context,var liststory:List<GetStoryReponse>,private val onItemClickListener: (Int) -> Unit):RecyclerView.Adapter<DisplayStoryAdapter.listStory> (){
    inner class listStory(itemView: View):RecyclerView.ViewHolder(itemView)
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listStory {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_display_story,parent,false)
        return listStory(view)
    }

    override fun onBindViewHolder(holder: listStory, position: Int) {
        holder.itemView.apply {
            // anh xa
            var avatarStory = findViewById<ImageView>(R.id.imgDisplayAvtStory)
            var imageStory = findViewById<ImageView>(R.id.imgDisplayStory)
            var txtNameStory = findViewById<TextView>(R.id.txtDisplayNameStory)
            var BtnClose = findViewById<ImageView>(R.id.closeStory)
            var BtnOptionStory = findViewById<ImageView>(R.id.optionStory)

            // binding info
            txtNameStory.setText(liststory[position].userName)
            val avatarUrl = liststory[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(avatarUrl, avatarStory)
            }
            val imageUrl = liststory[position].photo
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(imageUrl, imageStory)
            }

            // handle button
            BtnClose.setOnClickListener {
                onItemClickListener(position)
            }
            BtnOptionStory.setOnClickListener {
                showOptionsPopup(it)
            }
        }
    }

    private fun showOptionsPopup(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_option_story,popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.option1 -> {

                    true
                }
                R.id.option2 -> {
//                    popupMenu.dismiss()
                    true
                }
                // Xử lý các Option khác nếu cần
                else -> false
            }
        }
        popupMenu.gravity = Gravity.BOTTOM
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return liststory.size
    }
}