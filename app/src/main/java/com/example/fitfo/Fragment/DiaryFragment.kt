package com.example.fitfo.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.example.fitfo.DisplayStory
import com.example.fitfo.Interface.ApiService
import com.example.fitfo.Interface.RvChat
import com.example.fitfo.Models.GetPostReponse
import com.example.fitfo.Models.GetStoryReponse
import com.example.fitfo.databinding.FragmentDiaryBinding
import com.example.fitfo.test
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding
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

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        fetchPosts()
        fetchStorys()
        binding.addStory.setOnClickListener {
            launcher
        }

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
//        val adapterStory = StoryAdapter(liststory, object : RvChat {
//
//            override fun onClickchat(pos: Int) {
//                var intent = Intent(context, DisplayStory::class.java)
////                intent.putExtra("idChat","${listChats[pos]._id}")
//                startActivity(intent)
//            }
//        })
//        var listStory = binding.listStory
//        listStory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//        listStory.adapter = adapterStory
//        listStory.setHasFixedSize(true)

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
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        listStorys.addAll(storyResponse);
                        val adapterDs = StoryAdapter(listStorys, object : RvChat {
                            override fun onClickchat(pos: Int) {
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
                        Toast.makeText(context, "Không có chat nào", Toast.LENGTH_SHORT)
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

    private fun fetchPosts() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.175:3200/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.findPosts()
        call.enqueue(object : Callback<List<GetPostReponse>> {
            override fun onResponse(
                call: Call<List<GetPostReponse>>,
                response: Response<List<GetPostReponse>>
            ) {
                if (response.isSuccessful) {
                    val postsResponse = response.body()

                    if (!postsResponse.isNullOrEmpty()) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        listPosts.addAll(postsResponse);
                        Log.d("ddhh", listPosts[0].userName)

                        val adapterDs = postAdapter(listPosts)
                        var listpost = binding.listPort
                        listpost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                        listpost.adapter = adapterDs
                        listpost.setHasFixedSize(true)

                        // TODO: Thực hiện xử lý với thông tin người dùng
                    } else {
                        Toast.makeText(context, "Không có chat nào", Toast.LENGTH_SHORT)
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


    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}