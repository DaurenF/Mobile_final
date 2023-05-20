package com.example.demoendterm.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.demoendterm.Data
import com.example.demoendterm.PostAdapter
import com.example.demoendterm.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray

import org.json.JSONObject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var infoArrayList = ArrayList<Data>()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val button: Button = binding.button
        val fav: Button = binding.fav
        val img: ImageView = binding.imageView
        val button2: Button = binding.button2
        var sharedPreferences = requireContext().getSharedPreferences("post_prefs", Context.MODE_PRIVATE)
        var inf =""
        button2.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            infoArrayList.clear()
        }

        fav.setOnClickListener {
            if(inf.length>10){
                val dt = Data(inf,0,0)
                infoArrayList.add(dt)
                val editor = sharedPreferences.edit()
                val jsonArray = JSONArray()
                for (infos in infoArrayList) {
                    val jsonObject = JSONObject()
                    jsonObject.put("name", infos.name)
                    jsonObject.put("age", infos.age)
                    jsonObject.put("count", infos.count)
                    jsonArray.put(jsonObject)
                }
                editor.putString("post_data", jsonArray.toString())
                editor.apply()
            }

        }
        button.setOnClickListener {
            val url = "https://dog.ceo/api/breeds/image/random"
            val requestQueue = Volley.newRequestQueue(context)

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val jsonObject = JSONObject(response)
                    val name = jsonObject.getString("message")
                    inf=name;
                    Picasso.get().load(name).into(img)
                    textView.text = inf
                },
                { textView.text = "Error" })

            requestQueue.add(stringRequest)
        }
//        button.setOnClickListener {
//
//            val requestQueue = Volley.newRequestQueue(context)
//
//            val stringRequest = StringRequest(
//                Request.Method.GET, url,
//                { response ->
//                    val jsonObject = JSONObject(response)
//                    val name = jsonObject.getString("message")
//                    info = name
//                    textView.text = "$name"
//                    Picasso.get().load(name).into(img)
//                    val editor = sharedPreferences.edit()
//                    val jsonArray = JSONArray()
//                    for (info in infoArrayList) {
//                        val jsonObject = JSONObject()
//                        jsonObject.put("name", info.name)
//                        jsonArray.put(jsonObject)
//                    }
//                    editor.putString("post_data", jsonArray.toString())
//                    editor.apply()
//                },
//                { textView.text = "Error" })
//
//            requestQueue.add(stringRequest)
//        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}