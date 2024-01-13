package com.example.fitfo.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.DateFormat
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.CommentRequest
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.Models.findCommentResponse
import com.example.fitfo.Models.updatePostLikeRequest
import com.example.fitfo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class postAdapter(var listPost:List<GetPostReponse>, var myId: String)
    : RecyclerView.Adapter<postAdapter.ListPost>() {
    val dateFormat = DateFormat()
    inner class ListPost(itemView:View):RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPost {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_post,parent,false)
        return ListPost(view)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: ListPost, position: Int) {
        holder.itemView.apply {
            // ánh xạ
            var avatar = findViewById<ImageView>(R.id.post_avtar)
            var userName = findViewById<TextView>(R.id.post_of_user_name)
            var action = findViewById<TextView>(R.id.post_action)
            var timePost = findViewById<TextView>(R.id.post_time)
            var content = findViewById<TextView>(R.id.post_caption)
            var picturePost = findViewById<ImageView>(R.id.post_picture)
            var countLike = findViewById<TextView>(R.id.post_count_liked)
            var countComment = findViewById<TextView>(R.id.post_count_comment)
            var commentBtn = findViewById<TextView>(R.id.post_btn_comment)
            var optionBtn= findViewById<ImageView>(R.id.post_option)
            var love = findViewById<CheckBox>(R.id.post_btn_like)

            // truyền dữ liệu
            val avatarUrl = listPost[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(avatarUrl, avatar)
            }
            val pictureUrl = listPost[position].photo
            if (!pictureUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(pictureUrl, picturePost)
            }
            val author = listPost[position].author
            userName.text = listPost[position].userName
            action.text = listPost[position].action
            countComment.text = listPost[position].comment
            content.text = listPost[position].caption

            var time = listPost[position].createdAt ?: ""
            if (!time.isNullOrEmpty()) {
                var dateTimeFormated = dateFormat.toFormattedString(time)
                timePost.text = dateTimeFormated
            }

            // xem mình đã tim hay chưa
            var listLiked = listPost[position].like.toMutableList()
            if (listLiked != null) {
                val countLiked = listLiked.size.toString()
                countLike.text = countLiked
                val liked = myId in listLiked
                if (liked) {
                    love.isChecked = true
                }
            }

            // lắng nghe sự kiện ở checkbox love
            love.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked) {
                    // Nếu người dùng nhấn thích, tăng giá trị của biến countlike
                    listLiked.add(myId)
                    countLike.text = (listLiked.size).toString()
                    updateLike(listPost[position]._id, listLiked.toTypedArray())
                } else {
                    // Nếu người dùng bỏ thích, giảm giá trị của biến countlike
                    listLiked.remove(myId)
                    countLike.text = (listLiked.size).toString()
                    updateLike(listPost[position]._id, listLiked.toTypedArray())
                }
            }


            commentBtn.setOnClickListener {
                val dialog = BottomSheetDialog(context)
                var postId = listPost[position]._id
                var listComments: MutableList<findCommentResponse> = mutableListOf()
                val viewcommentport =
                    LayoutInflater.from(context).inflate(R.layout.activity_comment_post, null)
                var nocmt = viewcommentport.findViewById<LinearLayout>(R.id.nocmt)
                var edtCmt = viewcommentport.findViewById<EditText>(R.id.edtComment)
                var btnsend = viewcommentport.findViewById<ImageView>(R.id.btnSentComment)
                var commentRecyclerView =
                    viewcommentport.findViewById<RecyclerView>(R.id.commentRecyclerView)

                val apiService = RetrofitClient.apiService
                val call = apiService.findComment(postId)
                call.enqueue(object : Callback<List<findCommentResponse>> {
                    override fun onResponse(
                        call: Call<List<findCommentResponse>>,
                        response: Response<List<findCommentResponse>>
                    ) {
                        if (response.isSuccessful) {
                            val commentResponses = response.body()

                            if (!commentResponses.isNullOrEmpty()) {
                                listComments.addAll(commentResponses);
                                val adapterListComment = CommentAdapter(listComments, object : RvChat {
                                    override fun onClickchat(pos: Int) {
                                        val commentId = listComments[pos]._id
                                        listComments.removeAt(pos)
                                        showQuestionDialog(context,commentId)
                                    }
                                })
                                var listcomment = commentRecyclerView
                                listcomment.layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                listcomment.adapter = adapterListComment
                                listcomment.setHasFixedSize(true)
                                btnsend.setOnClickListener {
                                    if (edtCmt.length() == 0){
                                        Toast.makeText(context,"Hãy nhập bình luận của mình!",Toast.LENGTH_LONG).show()
                                    }else{
                                        val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
                                        val userName = sharedPreferences.getString("MY_NAME", "").toString()
                                        val author = sharedPreferences.getString("MY_ID", "").toString()
                                        val avatar = sharedPreferences.getString("MY_AVATAR", "").toString()
                                        val comment = edtCmt.text.toString().trim()
                                        val newComment = findCommentResponse("1",author, comment, postId, avatar, userName, "")
                                        val CommentRequest = CommentRequest(author, comment)

                                        val apiService = RetrofitClient.apiService
                                        val call = apiService.comment(postId, CommentRequest)

                                        call.enqueue(object : Callback<String> {
                                            override fun onResponse(
                                                call: Call<String>,
                                                response: Response<String>
                                            ) {
                                                if (response.isSuccessful) {
                                                    listComments.add(newComment)
                                                    // Bạn có thể muốn thông báo cho adapter về sự thay đổi dữ liệu
                                                    adapterListComment.notifyDataSetChanged()
                                                    // TODO: Xử lý message
                                                } else {
                                                    // TODO: Xử lý phản hồi không thành công
                                                    Toast.makeText(
                                                        context,
                                                        "Không thành công!",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }

                                            override fun onFailure(call: Call<String>, t: Throwable) {
                                                // TODO: Xử lý lỗi khi request không thành công
                                                Toast.makeText(context, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }
                                    edtCmt.setText("")
                                    edtCmt.setHint("Mời nhập bình luận")
                                }
                                // TODO: Thực hiện xử lý với thông tin người dùng
                            } else {
                                nocmt.visibility = View.VISIBLE
                                commentRecyclerView.visibility = View.GONE
                            }
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(context, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<findCommentResponse>>, t: Throwable) {
                        val errorMessage = "Lỗi: ${t.message}"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                })
                dialog.setCancelable(false)
                val btnclosecomment = viewcommentport.findViewById<ImageView>(R.id.btnCloseComment)
                viewcommentport?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels - 60
                viewcommentport?.minimumHeight = screenHeight
                val behavior = dialog.behavior
                behavior.peekHeight = screenHeight
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                btnclosecomment.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.setContentView(viewcommentport)
                dialog.show()
            }

            optionBtn.setOnClickListener {
                val dialog = BottomSheetDialog(context)
                val viewoptionport = LayoutInflater.from(context).inflate(R.layout.option_port, null)
                val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
                val myId = sharedPreferences.getString("MY_ID", "").toString()
                val btncloseport = viewoptionport.findViewById<ImageView>(R.id.btnCloseOptionPort)
                val btndeleteport = viewoptionport.findViewById<TextView>(R.id.deletePort)
                val line = viewoptionport.findViewById<View>(R.id.line)
                if (author != myId){
                    btndeleteport.visibility = View.GONE
                    line.visibility = View.GONE
                }
                btncloseport.setOnClickListener {
                    dialog.dismiss()
                }
                btndeleteport.setOnClickListener {
                    val apiService = RetrofitClient.apiService
                    val postId = listPost[position]._id
                    val call = apiService.deletePost(postId)

                    call.enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            Toast.makeText(context, response.body(), Toast.LENGTH_SHORT).show()
                            listPost
                            val adapterDs = postAdapter(listPost, myId)
                            adapterDs.notifyDataSetChanged()
                            dialog.dismiss()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            // Handle failure
                            Log.e("API Call", "Failed to delete post", t)
                            Toast.makeText(context, "Failed to delete post. Please try again.1", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    })
                }
                dialog.setCancelable(false)
                dialog.setContentView(viewoptionport)
                dialog.show()
            }
        }
    }

    fun showQuestionDialog(context: Context, commentId: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Xóa bình luận")
        alertDialogBuilder.setMessage("Bạn muốn xóa bình luận không?")
        alertDialogBuilder.setPositiveButton("Có") { dialog, which ->
            val apiService = RetrofitClient.apiService
            Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show()

            val call = apiService.deleteComment(commentId)
            call.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, response.body(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, "hi123", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "loi", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Handle failure
                    Log.e("API Call", "Failed to delete post", t)
                    Toast.makeText(context, "Failed to delete post. Please try again.1", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            })
        }

        // Thiết lập lựa chọn tiêu cực (negative button)
        alertDialogBuilder.setNegativeButton("Không") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    override fun getItemCount(): Int {
        return listPost.size
    }

    private fun updateLike(postId: String, array: Array<String>) {
        val apiService = RetrofitClient.apiService
        val call = apiService.updatePostLike(postId,updatePostLikeRequest(array))
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    Log.d("update like successfully................",
                        response.body().toString())
                } else {
                    Log.e("update like failed...............",
                        response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Error 250", t.message.toString() )
            }
        })
    }
}
