package com.example.demoendterm.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoendterm.Data
import com.example.demoendterm.PostAdapter
import com.example.demoendterm.R
import com.example.demoendterm.databinding.FragmentDashboardBinding


import org.json.JSONArray

class DashboardFragment : Fragment() {
    private var infoArrayList = ArrayList<Data>()
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val sharedPreferences = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sharedPreferences = requireContext().getSharedPreferences("post_prefs", Context.MODE_PRIVATE)
        val postJson = sharedPreferences.getString("post_data", "")
        if (!postJson.isNullOrEmpty()) {
            val jsonArray = JSONArray(postJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                infoArrayList.add(Data(name, 0, 0))
            }
        }
        val clear: Button = binding.clear
        clear.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            infoArrayList.clear()
            adapter.notifyDataSetChanged()
        }
        _binding?.root?.let { view ->
            recyclerView = view.findViewById(R.id.myRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            adapter = PostAdapter(infoArrayList)
            recyclerView.adapter = adapter
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}