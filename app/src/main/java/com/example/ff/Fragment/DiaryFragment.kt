package com.example.ff.Fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ff.Adapter.ListChatAdapter
import com.example.ff.Adapter.StoryAdapter
import com.example.ff.Adapter.postAdapter
import com.example.ff.ChatActivity
import com.example.ff.DisplayStory
import com.example.ff.Interface.RvChat
import com.example.ff.OutData.OutDataPost
import com.example.ff.OutData.OutData_Story
import com.example.ff.R
import com.example.ff.Test.NameChat
import com.example.ff.databinding.FragmentDiaryBinding
import com.example.ff.test
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding

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
        val adapterStory = StoryAdapter(liststory, object : RvChat {

            override fun onClickchat(pos: Int) {
                var intent = Intent(context, DisplayStory::class.java)
//                intent.putExtra("idChat","${listChats[pos]._id}")
                startActivity(intent)
            }
        })
        var listStory = binding.listStory
        listStory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        listStory.adapter = adapterStory
        listStory.setHasFixedSize(true)


        val listPost = mutableListOf<OutDataPost>()
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","hi mn",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","","10/3","",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","","10/3","dbchjgcfcjkdbsjhcvbhj",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","",R.drawable.i,"15","3"))
        listPost.add(OutDataPost(R.drawable.i,"Nguyễn Nhân","đã cập nhật ảnh đại diện","10/3","",R.drawable.i,"15","3"))
        val adapterPost= postAdapter(listPost)
        var Post = binding.listPort
        Post.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        Post.adapter = adapterPost
        Post.setHasFixedSize(true)
//        Log.e("adapter", binding.listTin.toString())
//        Toast.makeText(context, listtin[0].txtNameTin, Toast.LENGTH_LONG).show()
        binding.addStory.setOnClickListener {
            onClickRequestPermission()
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