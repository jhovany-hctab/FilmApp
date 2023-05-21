package org.bedu.filmapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.User

class UsersAdapter(
    private val dataSet: List<User>,
    private var onItemClicked: ((user: User) -> Unit)? = null
    ): RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageProfile: ImageView
        val nameProfile: TextView
        init {
            imageProfile = view.findViewById(R.id.image_profile_iv)
            nameProfile = view.findViewById(R.id.name_profile_tv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(dataSet.get(position).imageProfile).into(holder.imageProfile)
        holder.nameProfile.text = dataSet.get(position).username

        holder.imageProfile.setOnClickListener {
            onItemClicked?.let { it1 -> it1(dataSet[position]) }
        }
    }

    override fun getItemCount() = dataSet.size

}