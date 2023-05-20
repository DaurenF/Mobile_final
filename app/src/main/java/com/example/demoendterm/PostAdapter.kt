package com.example.demoendterm


import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class PostAdapter(val postModel: MutableList<Data>) : RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return PostViewHolder(view, parent.context, this)
    }

    override fun getItemCount(): Int = postModel.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(postModel[position])
    }
}

class PostViewHolder(itemView: View, private val context: Context, private val adapter: PostAdapter) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.TvTitle)
    private val button: Button = itemView.findViewById(R.id.button)

    fun bindView(data: Data) {
        tvTitle.text = data.name

        button.setOnClickListener {
            val infoArrayList = ArrayList<Data>()
            val sharedPreferences = context.getSharedPreferences("post_prefs", Context.MODE_PRIVATE)
            val postJson = sharedPreferences.getString("post_data", "")
            if (!postJson.isNullOrEmpty()) {
                val jsonArray = JSONArray(postJson)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("name")
                    infoArrayList.add(Data(name, 0, 0))
                }
            }

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                infoArrayList.removeAt(position)
                val newJsonArray = JSONArray()
                for (i in 0 until infoArrayList.size) {
                    val jsonObject = JSONObject()
                    jsonObject.put("name", infoArrayList[i].name)
                    newJsonArray.put(jsonObject)
                }
                val editor = sharedPreferences.edit()
                editor.putString("post_data", newJsonArray.toString())
                editor.apply()

                adapter.postModel.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }
    }


}

