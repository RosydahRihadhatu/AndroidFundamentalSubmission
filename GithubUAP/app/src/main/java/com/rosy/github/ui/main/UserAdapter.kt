package com.rosy.github.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosy.github.api.Const.EXTRA_USERNAME
import com.rosy.github.databinding.ItemUserBinding
import com.rosy.github.model.User
import com.rosy.github.ui.detail.DetailUserActivity
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val listUser = ArrayList<User>()

    inner class UserViewHolder(private val view: ItemUserBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(user: User) {
            with(view) {
                tvUsername.text = user.username
                Glide.with(itemView.context).load(user.avatar).into(ivPhoto)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailUserActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, user.username)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

   override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       holder.bind(listUser[position])
   }

    override fun getItemCount(): Int = listUser.size

    fun setAllUser(user: List<User>) {
        listUser.apply {
            clear()
            addAll(user)
        }
        notifyDataSetChanged()
    }
}