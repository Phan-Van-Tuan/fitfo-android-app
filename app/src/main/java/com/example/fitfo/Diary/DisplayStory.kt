package com.example.fitfo.Diary

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfo.Adapter.DisplayStoryAdapter
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.databinding.ActivityDisplayStoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayStory : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayStoryBinding
    private var listStorys: MutableList<GetStoryReponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchStorys()
    }

    private fun fetchStorys() {
        val apiService = RetrofitClient.apiService
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
                                    handler.postDelayed(runnable, 10000)
                                }
                            }
                        })

                        // Start auto-scroll
                        handler.postDelayed(runnable, 10000)

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