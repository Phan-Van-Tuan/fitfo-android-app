package com.example.fitfo.Fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfo.Adapter.StoryAdapter
import com.example.fitfo.Adapter.postAdapter
import com.example.fitfo.Define.CallApi.RetrofitClient
import com.example.fitfo.Define.FullScreenBottomSheetDialog
import com.example.fitfo.Diary.CreatePost
import com.example.fitfo.Diary.CreateStory
import com.example.fitfo.Diary.DisplayStory
import com.example.fitfo.Interface.RecyclerViewOnClick
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentDiaryBinding
import com.example.fitfo.test
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var loader: FullScreenBottomSheetDialog? = null
    private var listPosts: MutableList<GetPostReponse> = mutableListOf()
    private var listStorys: MutableList<GetStoryReponse> = mutableListOf()

    val REQUEST_CODE = 10
    private val launcher = registerImagePicker { images ->
        // selected images
        if (images.isNotEmpty()) {
            val image = images[0]
            var intent = Intent(context, test::class.java)
            intent.putExtra("uriStory",image.uri.toString())
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
        val myId = sharedPreferences.getString("MY_ID", null).toString()

//        loader = FullScreenBottomSheetDialog(requireActivity())
//        loader?.showLoading(R.layout.layout_loading)
        fetchPosts(myId)
        fetchStorys()

        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }

        binding.addStory.setOnClickListener {
            var intent = Intent(context, CreateStory::class.java)
            startActivity(intent)
        }

        binding.createPost.setOnClickListener {
            var intent = Intent(context, CreatePost::class.java)
            startActivity(intent)
        }
    }

    private fun openGallery() {
        launcher.launch()
    }

    private fun onClickRequestPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireActivity(), permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission),
                REQUEST_CODE
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform tasks related to the gallery here
                openGallery()
            } else {
                // Permission not granted, handle it (e.g., show a message to the user)
            }
        }
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
                        val adapterDs = StoryAdapter(listStorys, object : RecyclerViewOnClick {
                            override fun onClickItem(pos: Int) {
                                var intent = Intent(context, DisplayStory::class.java)
                                intent.putExtra("pos",pos)
                                startActivity(intent)
                            }
                        })
                        var liststory = binding.listStory
                        liststory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                        liststory.adapter = adapterDs
                        liststory.setHasFixedSize(true)
                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(context, "Chưa có tin nào", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(context, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetStoryReponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchPosts(myId: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.findPosts(myId)
        call.enqueue(object : Callback<List<GetPostReponse>> {
            override fun onResponse(
                call: Call<List<GetPostReponse>>,
                response: Response<List<GetPostReponse>>
            ) {
                if (response.isSuccessful) {
                    val postsResponse = response.body()
                    if (!postsResponse.isNullOrEmpty()) {
                        listPosts.addAll(postsResponse)
                        listPosts.shuffle()
                        val adapterDs = postAdapter(listPosts, myId)
                        var listpost = binding.listPost
                        listpost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                        listpost.adapter = adapterDs
                        listpost.setHasFixedSize(true)
                        // TODO: Thực hiện xử lý với thông tin người

                    } else {
                        Toast.makeText(context, "Chưa có bài viết nào", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(context, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<GetPostReponse>>, t: Throwable) {
                val errorMessage = "Lỗi: ${t.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}

