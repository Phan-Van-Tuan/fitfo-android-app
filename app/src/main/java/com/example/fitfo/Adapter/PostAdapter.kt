package com.example.fitfo.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.fitfo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class postAdapter(var listPost:List<GetPostReponse>): RecyclerView.Adapter<postAdapter.ListPost>() {
    inner class ListPost(itemView:View):RecyclerView.ViewHolder(itemView)
    val dateFormat = DateFormat()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPost {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_post,parent,false)
        return ListPost(view)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: ListPost, position: Int) {
        holder.itemView.apply {
            // ánh xạ
            var imgAvtPost = findViewById<ImageView>(R.id.imgAvtPost)
            var txtNamePost = findViewById<TextView>(R.id.txtNamePort)
            var txtCause = findViewById<TextView>(R.id.txtCause)
            var txtTimePost = findViewById<TextView>(R.id.txtTimeport)
            var txtStatus = findViewById<TextView>(R.id.txtStatus)
            var picturePost = findViewById<ImageView>(R.id.picture_post)
            var count_heart = findViewById<TextView>(R.id.count_heart)
            var countCmt = findViewById<TextView>(R.id.countCmt)
            var cmt = findViewById<TextView>(R.id.ic_cmt)
            var btnOptionPort= findViewById<ImageView>(R.id.optionPort)

            // truyền dữ liệu
            val avatarUrl = listPost[position].avatar
            if (!avatarUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(avatarUrl, imgAvtPost)
            }
            val pictureUrl = listPost[position].photo
            if (!pictureUrl.isNullOrEmpty() ) {
                ImageUtils.displayImage2(pictureUrl, picturePost)
            }
            val author = listPost[position].author
            txtNamePost.setText(listPost[position].userName)
            txtCause.setText(listPost[position].action)
            countCmt.setText(listPost[position].comment)

//            var dateTimeFormated = dateFormat.toFormattedString(listPost[position].createdAt)
//            if (!dateTimeFormated.isNullOrEmpty() ) {
//                txtTimePost.setText(dateTimeFormated)
//            }

            txtStatus.setText(listPost[position].caption)
            if (listPost[position].like != null) {
                val countlike = listPost[position].like.size.toString()
                count_heart.setText(countlike)
                // Tiếp tục xử lý dựa trên kích thước của danh sách
            } else {
                count_heart.setText("0")
            }

            cmt.setOnClickListener {
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
                                        val comment = edtCmt.text.toString()
                                        val newComment = findCommentResponse("1",author, comment, postId, avatar, userName.toString())
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

            btnOptionPort.setOnClickListener {
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
                            response: retrofit2.Response<String>
                        ) {
                            Toast.makeText(context, response.body(), Toast.LENGTH_SHORT).show()
                            listPost
                            val adapterDs = postAdapter(listPost)
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
//            countCmt.setText(listPost[position].countCmt)
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
}
