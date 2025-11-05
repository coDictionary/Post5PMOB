package com.farhan164.post5pmob.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhan164.post5pmob.R
import com.farhan164.post5pmob.model.Post
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import com.farhan164.post5pmob.databinding.ItemPostBinding

class PostAdapter(
    private val onMenuClick: (Post, View) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, onMenuClick: (Post, View) -> Unit) {
            binding.tvPostUsername.text = post.username
            binding.tvPostCaption.text = post.caption
            Glide.with(binding.root.context)
                .load(post.imageUri.toUri())
                .into(binding.ivPostImage)
            Glide.with(binding.root.context)
                .load(post.profileUri.toUri())
                .into(binding.ivPostProfile)

            binding.ivPostMenu.setOnClickListener {
                onMenuClick(post, binding.ivPostMenu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post, onMenuClick)
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    // ... (Tidak ada perubahan di sini)
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}