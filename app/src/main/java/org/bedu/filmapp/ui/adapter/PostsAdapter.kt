package org.bedu.filmapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Post

class PostsAdapter(
    private val dataSet: List<Post>,
    private var onItemClicked: ((posy: Post) -> Unit)? = null
    ): RecyclerView.Adapter<PostsAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imagePost: ImageView
        init {
            imagePost = view.findViewById(R.id.image_iv)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(dataSet[position].imagePost).into(holder.imagePost)
        holder.imagePost.setOnClickListener {
            onItemClicked?.let { it1 -> it1(dataSet[position]) }
        }
    }

    override fun getItemCount() = dataSet.size

}