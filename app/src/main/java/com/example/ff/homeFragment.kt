package com.example.ff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ff.databinding.FragmentHomeBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ds = mutableListOf<OutData>()
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihbbjkjh", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihibhjbjb", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        ds.add(OutData(R.drawable.a, "Tuấn", "hihi", "10 phút"))
        val adapterDs = listAdapter(ds)
        var dsChat = binding.dsChat
        dsChat.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        dsChat.adapter = adapterDs
        dsChat.setHasFixedSize(true)
//        Log.e("adapter", binding.dsChat.toString())
//        Toast.makeText(context, ds[0].txtName, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            homeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }
}