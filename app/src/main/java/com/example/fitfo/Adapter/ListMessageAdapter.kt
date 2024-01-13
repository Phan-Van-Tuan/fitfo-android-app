package com.example.fitfo.Adapter

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.fitfo.Models.findMessageResponse
import com.example.fitfo.R
import com.example.fitfo.Define.DateFormat
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Interface.RvChat


class ListMessageAdapter(
    var listmessage: MutableList<findMessageResponse>, private
    var myId: String
) : RecyclerView.Adapter<ListMessageAdapter.listMessage>() {
    private val dateFormat = DateFormat()
    private var mediaPlayer: MediaPlayer? = null
    private var currentAudioAnimation: LottieAnimationView? = null

    // Định nghĩa các hằng số cho các loại tin nhắn
    companion object {
        const val TYPE_AUDIO = "audio"
        const val TYPE_PHOTO = "photo"
        const val TYPE_MESSAGE = "message"
    }
    inner class listMessage(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listMessage {

        if (viewType == 2) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_received_message, parent, false)
            return listMessage(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_sent_message, parent, false)
            return listMessage(view)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: listMessage, position: Int) {
        holder.itemView.apply {
            val txtMessage = findViewById<TextView>(R.id.txtMessage)
            val txtDateTime = findViewById<TextView>(R.id.txtDateTime)
            val imgProfile = findViewById<ImageView>(R.id.imgProfile)
            val userAvatar = UserInfo.userAvatar
            val audio = findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.display_audio)
            val rcImage = findViewById<RecyclerView>(R.id.display_image)

            if (!userAvatar.isNullOrEmpty()) {
                ImageUtils.displayImage2(userAvatar, imgProfile)
            }

            // Sử dụng phương thức when để kiểm tra biến type
            when (listmessage[position].type) {
                // Nếu type là TYPE_AUDIO
                TYPE_AUDIO -> {
                    // Thực hiện các hành động cho loại tin nhắn âm thanh
                    txtMessage.visibility = View.GONE
                    rcImage.visibility = View.GONE
                    audio.visibility = View.VISIBLE
                    audio.setOnClickListener {
                        var audioUrl = listmessage[position].title
                        startPlaying(audioUrl, audio)
                    }
                }
                // Nếu type là TYPE_PHOTO
                TYPE_PHOTO -> {
                    // Split the string by commas
                    txtMessage.visibility = View.GONE
                    audio.visibility = View.GONE
                    rcImage.visibility = View.VISIBLE
                    val urlList = listmessage[position].title.split(",")

                    // Filter out any empty strings
                    val nonEmptyUrls = urlList.filter { it.isNotBlank() }
                    val image: MutableList<String> = mutableListOf()

                    // Thêm các URL vào danh sách
                    image.addAll(nonEmptyUrls)
                    val adapterImage = ImageAdapter(image, object : RvChat {
                        override fun onClickchat(pos: Int) {
                        }
                    })
                    val listImage = rcImage
                    listImage.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
                    listImage.adapter = adapterImage
                    }
                // Nếu type là TYPE_MESSAGE hoặc bất kỳ giá trị nào khác
                else -> {
                    // Thực hiện các hành động cho loại tin nhắn văn bản
                    txtMessage.visibility = View.VISIBLE
                    audio.visibility = View.GONE
                    rcImage.visibility = View.GONE
                    txtMessage.setText(listmessage[position].title)
                }
            }

            var dateTimeFormated = dateFormat.toFormattedString(listmessage[position].createdAt)
            txtDateTime.setText(dateTimeFormated)
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (listmessage[position].senderId == myId) {
            return 1
        } else {
            return 2
        }
    }
    private fun startPlaying(audioUrl: String, audio: LottieAnimationView) {
        stopPlaying()
        currentAudioAnimation = audio
        currentAudioAnimation?.playAnimation()
        mediaPlayer = MediaPlayer()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        mediaPlayer?.setAudioAttributes(audioAttributes)

        try {
            mediaPlayer?.setDataSource(audioUrl)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                currentAudioAnimation?.pauseAnimation()
                currentAudioAnimation?.progress = 0F
                currentAudioAnimation = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            currentAudioAnimation?.pauseAnimation()
            currentAudioAnimation?.progress = 0F
            currentAudioAnimation = null
        }
    }

    private fun stopPlaying() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                currentAudioAnimation?.pauseAnimation()
                currentAudioAnimation?.progress = 0F
                currentAudioAnimation = null
            }
            it.release()
        }
    }

    override fun getItemCount(): Int {
        return listmessage.size
    }
}