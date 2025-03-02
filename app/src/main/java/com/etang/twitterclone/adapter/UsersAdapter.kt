package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.User

class UsersAdapter(private val onUserClicked: (userId: Int) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    fun submitList(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], onUserClicked)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProfilePic: ImageView = itemView.findViewById(R.id.ivProfilePic)
        private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        private val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)

        fun bind(user: User, onUserClicked: (userId: Int) -> Unit) {
            tvUsername.text = "@${user.username}"
            tvFullName.text = "${user.firstName} ${user.lastName}"

            Glide.with(itemView.context)
                .load(
                    user.profilePictureUrl
                        ?: "https://abs.twimg.com/sticky/default_profile_images/default_profile_400x400.png"
                )
                .circleCrop()
                .into(ivProfilePic)


            itemView.setOnClickListener { onUserClicked(user.id) }
        }
    }
}
