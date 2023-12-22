package com.example.ff

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ff.Adapter.DisplayStoryAdapter
import com.example.ff.Adapter.StoryAdapter
import com.example.ff.OutData.OutData_Story
import com.example.ff.databinding.ActivityDisplayStoryBinding

class DisplayStory : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val liststory = mutableListOf<OutData_Story>()
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        liststory.add(OutData_Story(R.drawable.k,R.drawable.i,"Phan Tuấn"))
        val storyAdapter = DisplayStoryAdapter(this,liststory,onItemClickListener)
        val listdisplay = binding.list
        listdisplay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        listdisplay.adapter = storyAdapter
        listdisplay.setHasFixedSize(true)
        // Set up auto-scroll
        val handler = android.os.Handler()
        val runnable = Runnable {
            val nextItem = (listdisplay.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1
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
                    handler.postDelayed(runnable, 3000)
                }
            }
        })

        // Start auto-scroll
        handler.postDelayed(runnable, 3000)
    }
    private val onItemClickListener: (Int) -> Unit = { position ->
        setResult(Activity.RESULT_OK)
        finish()
    }
}