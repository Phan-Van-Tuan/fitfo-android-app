package com.example.ff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ff.R.layout.layout_ds


class listAdapter(var ds: MutableList<OutData>): RecyclerView.Adapter<listAdapter.listchat>() {
inner class listchat(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listchat {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_ds,parent,false)
        return listchat(view)
    }

    override fun onBindViewHolder(holder: listchat, position: Int) {
        holder.itemView.apply {
            var txtName = findViewById<TextView>(R.id.txtName)
            var txtTt = findViewById<TextView>(R.id.txtTt)
            var txtTime = findViewById<TextView>(R.id.txtTime)
            var imgAvt = findViewById<ImageView>(R.id.imgAvt)
            txtName.setText(ds[position].txtName)
            txtTt.setText(ds[position].txtTt)
            txtTime.setText(ds[position].txtTime)
            imgAvt.setImageResource(ds[position].imgAvt)
        }
    }

    override fun getItemCount(): Int {
        return ds.size
    }
}