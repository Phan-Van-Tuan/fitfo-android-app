package com.example.ff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.R.layout.layout_tin

class tinAdapter( var listtin:List<OutData_Tin>):RecyclerView.Adapter<tinAdapter.listTin> (){
    inner class listTin(itemView: View):RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listTin {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_tin,parent,false)
        return listTin(view)
    }

    override fun onBindViewHolder(holder: listTin, position: Int) {
        holder.itemView.apply {
            var imgAvtTin = findViewById<ImageView>(R.id.imgAvtTin)
            var imgTin = findViewById<ImageView>(R.id.imgTin)
            var txtNameTin = findViewById<TextView>(R.id.txtNameTin)
            imgAvtTin.setImageResource(listtin[position].imgAvtTin)
            imgTin.setImageResource(listtin[position].imgTin)
            txtNameTin.setText(listtin[position].txtNameTin)
        }
    }



    override fun getItemCount(): Int {
        return listtin.size
    }
}