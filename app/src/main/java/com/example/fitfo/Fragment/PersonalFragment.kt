package com.example.fitfo.Fragment

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitfo.Account_Security
import com.example.fitfo.Define.ImageUtils
import com.example.fitfo.MainActivity
import com.example.fitfo.Profile.PersonalActivity
import com.example.fitfo.Search
import com.example.fitfo.databinding.FragmentPersonalBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalFragment : Fragment() {
    private lateinit var binding: FragmentPersonalBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
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
        binding = FragmentPersonalBinding.inflate(inflater, container, false)



        return binding.root
//        a muốn gọi shared vào đây
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            var intent = Intent(context, Search::class.java)
            startActivity(intent)
        }

        binding.personal.setOnClickListener {
            var intent = Intent(context, PersonalActivity::class.java)
            startActivity(intent)
        }
        binding.accountAndSecurity.setOnClickListener {
            var intent = Intent(context, Account_Security::class.java)
            startActivity(intent)
        }

        binding.btnLogOut.setOnClickListener {
            removePrivateData()
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        sharedPreferences = requireActivity()?.getSharedPreferences("data", Context.MODE_PRIVATE)!!
        val avatarUrl = sharedPreferences.getString("MY_AVATAR", "")
        if (!avatarUrl.isNullOrEmpty() ) {
            ImageUtils.displayImage2(avatarUrl, binding.imgAvt)
        }
        binding.textName.setText(sharedPreferences.getString("MY_NAME", ""))

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PersonalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun removePrivateData() {
        sharedPreferences = requireActivity()?.getSharedPreferences("data", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()

        editor.remove("MY_ID")
        editor.remove("MY_NAME")
        editor.remove("MY_PHONE_NUMBER")
        editor.remove("MY_AVATAR")
        editor.remove("ACCESS_TOKEN")

        editor.apply()
    }
}