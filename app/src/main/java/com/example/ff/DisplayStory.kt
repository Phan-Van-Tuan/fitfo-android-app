package com.example.ff

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ff.Adapter.DisplayStoryAdapter
import com.example.ff.Adapter.StoryAdapter
import com.example.ff.Interface.ApiService
import com.example.ff.Interface.RvChat
import com.example.ff.Models.GetStoryReponse
import com.example.ff.OutData.OutData_Story
import com.example.ff.databinding.ActivityDisplayStoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects

class DisplayStory : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayStoryBinding
    private var listStorys: MutableList<GetStoryReponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchStorys()

//        val liststory = mutableListOf<OutData_Story>()
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
//        val storyAdapter = DisplayStoryAdapter(this,liststory,onItemClickListener)
//        val listdisplay = binding.list
//        listdisplay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        listdisplay.adapter = storyAdapter
//        listdisplay.setHasFixedSize(true)
//        // Set up auto-scroll
//        val handler = android.os.Handler()
//        val runnable = Runnable {
//            val nextItem = (listdisplay.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1
//            if (nextItem < storyAdapter.itemCount) {
//                listdisplay.smoothScrollToPosition(nextItem)
//            } else {
//                listdisplay.smoothScrollToPosition(0)
//            }
//        }

//        listdisplay.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    // Delay the auto-scroll for a specific time (e.g., 3 seconds)
//                    handler.postDelayed(runnable, 15000)
//                }
//            }
//        })
//
//        // Start auto-scroll
//        handler.postDelayed(runnable, 15000)
    }

    private fun fetchStorys() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.175:3200/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.findStory()
        call.enqueue(object : Callback<List<GetStoryReponse>> {
            override fun onResponse(
                call: Call<List<GetStoryReponse>>,
                response: Response<List<GetStoryReponse>>
            ) {
                if (response.isSuccessful) {
                    val storyResponse = response.body()

                    if (!storyResponse.isNullOrEmpty()) {
                        listStorys.addAll(storyResponse);
                        val position = intent.getIntExtra("pos", -1)
                        Log.d("pos", position.toString())
                        val storyAdapter = DisplayStoryAdapter(this@DisplayStory, listStorys,onItemClickListener)
                        storyAdapter.setSelectedPosition(position)
                        val listdisplay = binding.list
                        listdisplay.layoutManager = LinearLayoutManager(this@DisplayStory, LinearLayoutManager.HORIZONTAL, false)
                        listdisplay.adapter = storyAdapter
                        listdisplay.setHasFixedSize(true)
                        listdisplay.scrollToPosition(position)

                        // Set up auto-scroll
                        val handler = android.os.Handler()
                        val runnable = Runnable {
                            val nextItem =
                                (listdisplay.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
                            if (nextItem < storyAdapter.itemCount) {
                                listdisplay.smoothScrollToPosition(nextItem)
                            } else {
                                listdisplay.smoothScrollToPosition(0)
                            }
                        }
                        listdisplay.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    // Delay the auto-scroll for a specific time (e.g., 3 seconds)
                                    handler.postDelayed(runnable, 15000)
                                }
                            }
                        })

                        // Start auto-scroll
                        handler.postDelayed(runnable, 15000)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(this@DisplayStory, "Không có chat nào", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@DisplayStory, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetStoryReponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(this@DisplayStory, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private val onItemClickListener: (Int) -> Unit = { position ->
        setResult(Activity.RESULT_OK)
        finish()
    }
}