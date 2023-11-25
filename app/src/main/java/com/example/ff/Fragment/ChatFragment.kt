package com.example.ff.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ff.Adapter.ListChatAdapter
import com.example.ff.ChatActivity
import com.example.ff.OutData.OutDataChat
import com.example.ff.R
import com.example.ff.Interface.RvChat
import com.example.ff.databinding.FragmentChatBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
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
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listchat = mutableListOf<OutDataChat>()
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Nhân", "hihbbjkjh", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihibhjbjb", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        listchat.add(OutDataChat(R.drawable.a, "Tuấn", "hihi", "10 phút"))

        val adapterDs = ListChatAdapter(listchat, object : RvChat {
            override fun onClickchat(pos: Int) {
//                Toast.makeText(context, "bạn đã click vào ${ds[pos].txtName}", Toast.LENGTH_SHORT).show()
//                truyền đến màn hình chat
                var intent = Intent(context, ChatActivity::class.java)
                startActivity(intent)
            }

        })
        var dsChat = binding.dsChat
        dsChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        dsChat.adapter = adapterDs
        dsChat.setHasFixedSize(true)
//        Log.e("adapter", binding.dsChat.toString())
//        Toast.makeText(context, ds[0].txtName, Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}