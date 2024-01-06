package com.example.test_firebase

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageLoader {

    companion object {
        fun loadImage(context: Context, imageUrl: String, imageView: ImageView) {
            Picasso.get()
                .load(imageUrl)
                .into(imageView)
        }
    }
}