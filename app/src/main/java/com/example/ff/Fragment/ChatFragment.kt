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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
                var intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("txtNameChat","${listchat[pos].txtNameChat}")
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