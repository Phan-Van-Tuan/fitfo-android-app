package com.example.ff.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ff.Adapter.StoryAdapter
import com.example.ff.Adapter.postAdapter
import com.example.ff.Adapter.ListChatAdapter
import com.example.ff.OutData.OutDataPost
import com.example.ff.OutData.OutData_Story
import com.example.ff.R
import com.example.ff.databinding.FragmentDiaryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding
    // TODO: Rename and change types of parameters
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
        val adapterStory = StoryAdapter(liststory)
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
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiaryFragment.
         */
        // TODO: Rename and change types and number of parameters
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