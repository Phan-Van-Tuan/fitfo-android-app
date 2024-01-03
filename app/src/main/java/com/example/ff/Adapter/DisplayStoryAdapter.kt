package com.example.ff.Adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.Interface.RvChat
import com.example.ff.Models.GetStoryReponse
import com.example.ff.OutData.OutData_Story
import com.example.ff.R

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
            var imgAvtStory = findViewById<ImageView>(R.id.imgDisplayAvtStory)
            var imgStory = findViewById<ImageView>(R.id.imgDisplayStory)
            var txtNameStory = findViewById<TextView>(R.id.txtDisplayNameStory)
            var BtnClose = findViewById<ImageView>(R.id.closeStory)
            var BtnOptionStory = findViewById<ImageView>(R.id.optionStory)
//            imgAvtStory.setImageResource(liststory[position].imgAvtStory)
//            imgStory.setImageResource(liststory[position].imgStory)
            txtNameStory.setText(liststory[position]._id)

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