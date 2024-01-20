package com.example.fitfo.Adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.fitfo.Models.findMessageResponse
import com.example.fitfo.R
import com.example.fitfo.Define.DateFormat
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Define.UserInfo
import com.example.fitfo.Interface.RecyclerViewOnClick
import com.example.fitfo.Interface.RecyclerViewOnLongClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class ListMessageAdapter(
    private var listMessage: MutableList<findMessageResponse>,
    private var myId: String,
    val ROLC: RecyclerViewOnLongClick
) : RecyclerView.Adapter<ListMessageAdapter.ListMessage>() {
    private val dateFormat = DateFormat()
    private var mediaPlayer: MediaPlayer? = null
    private var currentAudioAnimation: LottieAnimationView? = null

    // Định nghĩa các hằng số cho các loại tin nhắn
    companion object {
        const val TYPE_AUDIO = "audio"
        const val TYPE_PHOTO = "photo"
        const val TYPE_MESSAGE = "message"
    }
    inner class ListMessage(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMessage {

        return if (viewType == 2) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_received_message, parent, false)
            ListMessage(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_sent_message, parent, false)
            ListMessage(view)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: ListMessage,
        @SuppressLint("RecyclerView") position: Int) {
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
            when (listMessage[position].type) {
                // Nếu type là TYPE_AUDIO
                TYPE_AUDIO -> {
                    // Thực hiện các hành động cho loại tin nhắn âm thanh
                    txtMessage.visibility = View.GONE
                    rcImage.visibility = View.GONE
                    audio.visibility = View.VISIBLE
                    audio.setOnClickListener {
                        var audioUrl = listMessage[position].title
                        startPlaying(audioUrl, audio)
                    }
                    audio.setOnLongClickListener {
                        if(getItemViewType(position) == 1) {
                            ROLC.onLongClickItem(position)
                        }
                        true
                    }
                }
                // Nếu type là TYPE_PHOTO
                TYPE_PHOTO -> {
                    // Split the string by commas
                    txtMessage.visibility = View.GONE
                    audio.visibility = View.GONE
                    rcImage.visibility = View.VISIBLE
                    val urlList = listMessage[position].title.split(",")

                    // Filter out any empty strings
                    val nonEmptyUrls = urlList.filter { it.isNotBlank() }
                    val image: MutableList<String> = mutableListOf()
                    // Thêm các URL vào danh sách
                    image.addAll(nonEmptyUrls)
                    val adapterImage = ImageAdapter(image, object : RecyclerViewOnClick {
                        override fun onClickItem(pos: Int) {
                            val dialog = BottomSheetDialog(context,R.style.CustomBottomSheetDialog)
                            val viewimage = LayoutInflater.from(context).inflate(R.layout.layout_image, null)
                            var btncloseimage = viewimage.findViewById<ImageView>(R.id.btn_close)
                            var rcimage = viewimage.findViewById<RecyclerView>(R.id.rc_display_image)
                            val listimage = rcimage
                            val imageAdapter = DisplayImageAdapter(image)
                            listimage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            listimage.adapter = imageAdapter
                            listimage.setHasFixedSize(true)
                            listimage.scrollToPosition(pos)
                            val snapHelper = PagerSnapHelper()
                            snapHelper.attachToRecyclerView(listimage)
                            dialog.setCancelable(false)
                            viewimage?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
                            val screenHeight = Resources.getSystem().displayMetrics.heightPixels - 60
                            viewimage?.minimumHeight = screenHeight
                            val behavior = dialog.behavior
                            behavior.peekHeight = screenHeight
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            btncloseimage.setOnClickListener {
                                Log.d("btncloseimage", "Nút đóng được nhấp")
                                dialog.dismiss()
                            }
                            dialog.setContentView(viewimage)
                            dialog.show()
                        }
                    },object : RecyclerViewOnLongClick{
                        override fun onLongClickItem(pos: Int) {
                            if(getItemViewType(position) == 1) {
                                ROLC.onLongClickItem(position)
                            }
                            true
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
                    txtMessage.setText(listMessage[position].title)
                    holder.itemView.setOnLongClickListener {
                        if(getItemViewType(position) == 1) {
                            ROLC.onLongClickItem(position)
                        }
                        true
                    }
                }
            }

            var dateTimeFormated = dateFormat.toFormattedString(listMessage[position].createdAt)
            txtDateTime.setText(dateTimeFormated)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (listMessage[position].senderId == myId) {
            1
        } else {
            2
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
        return listMessage.size
    }
}