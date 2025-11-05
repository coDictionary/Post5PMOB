package com.farhan164.post5pmob.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhan164.post5pmob.R
import com.farhan164.post5pmob.databinding.ActivityMainBinding
import com.farhan164.post5pmob.databinding.DialogAddPostBinding
import com.farhan164.post5pmob.model.Post
import com.farhan164.post5pmob.model.Story
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val postViewModel: PostViewModel by viewModels()

    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter

    private var selectedImageUri: Uri? = null

    private var dialogPreviewImage: ImageView? = null
    private var dialogPreviewText: TextView? = null

    private val pickImageLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                // Tampilkan preview di dialog
                dialogPreviewImage?.let { iv ->
                    Glide.with(this).load(it).into(iv)
                    iv.visibility = View.VISIBLE
                }
                dialogPreviewText?.visibility = View.GONE
                Toast.makeText(this, "Berhasil menambahkan gambar", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set root dari binding sebagai content view


        setupStoryRecyclerView()
        setupPostRecyclerView()

        postViewModel.allPosts.observe(this) { posts ->
            posts?.let { postAdapter.submitList(it) }
        }

        binding.fabAddPost.setOnClickListener {
            showAddOrEditPostDialog(null) // null berarti "Add New"
        }
    }

    private fun setupStoryRecyclerView() {
        val dummyStories = loadDummyStories()
        storyAdapter = StoryAdapter(dummyStories)
        // Akses RecyclerView menggunakan binding
        binding.rvStories.adapter = storyAdapter
        binding.rvStories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupPostRecyclerView() {
        // Saat menu (titik tiga) di klik
        postAdapter = PostAdapter { post, view ->
            showPostMenu(post, view)
        }
        // Akses RecyclerView menggunakan binding
        binding.rvPosts.adapter = postAdapter
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
    }

    private fun loadDummyStories(): List<Story> {
        return listOf(
            Story("intan_dwi", R.drawable.avatars_female),
            Story("minda_04", R.drawable.avatars_male),
            Story("rubi_comm", R.drawable.avatars_male_glasses),
            Story("amelia", R.drawable.avatars_female_glasses),
            Story("rizka", R.drawable.avatars_male_glasses),
            Story("citra_l", R.drawable.avatars_female_glasses)
        )
    }

    private fun showPostMenu(post: Post, anchorView: View) {
        // ... (Fungsi ini tidak berubah)
        val popup = PopupMenu(this, anchorView)
        popup.inflate(R.menu.menu_post_options)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    showAddOrEditPostDialog(post)
                    true
                }
                R.id.menu_delete -> {
                    showDeleteConfirmDialog(post)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showDeleteConfirmDialog(post: Post) {
        // ... (Fungsi ini tidak berubah)
        AlertDialog.Builder(this)
            .setTitle("Hapus Postingan")
            .setMessage("Anda yakin ingin menghapus postingan ini?")
            .setPositiveButton("Ya") { _, _ ->
                postViewModel.delete(post)
                Toast.makeText(this, "Postingan dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun showAddOrEditPostDialog(post: Post?) {
        val dialogBinding = DialogAddPostBinding.inflate(LayoutInflater.from(this))

        dialogPreviewImage = dialogBinding.ivPreview
        dialogPreviewText = dialogBinding.tvImagePlaceholder

        selectedImageUri = null

        // 3. Cek mode: Edit atau Add
        if (post != null) {
            // Mode Edit - Akses views via dialogBinding
            dialogBinding.tvDialogTitle.text = "Edit Post"
            dialogBinding.etUsername.setText(post.username)
            dialogBinding.etCaption.setText(post.caption)
            selectedImageUri = Uri.parse(post.imageUri)
            Glide.with(this).load(selectedImageUri).into(dialogBinding.ivPreview)
            dialogBinding.ivPreview.visibility = View.VISIBLE
            dialogBinding.tvImagePlaceholder.visibility = View.GONE
            dialogBinding.btnSave.text = "Update"
        } else {
            dialogBinding.tvDialogTitle.text = "Tambah Post Baru"
            dialogBinding.ivPreview.visibility = View.GONE
            dialogBinding.tvImagePlaceholder.visibility = View.VISIBLE
            dialogBinding.btnSave.text = "Simpan"
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnPickImage.setOnClickListener {
            openGallery()
        }

        dialogBinding.btnSave.setOnClickListener {
            val username = dialogBinding.etUsername.text.toString().trim()
            val caption = dialogBinding.etCaption.text.toString().trim()

            // Validasi input
            if (username.isEmpty() || caption.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Isi semua kolom dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tentukan apakah akan insert atau update
            if (post != null) {
                // Update post yang ada
                val updatedPost = post.copy(
                    username = username,
                    caption = caption,
                    imageUri = selectedImageUri.toString()
                )
                postViewModel.update(updatedPost)
                Toast.makeText(this, "Postingan diupdate", Toast.LENGTH_SHORT).show()
            } else {
                // Insert post baru
                val newPost = Post(
                    username = username,
                    caption = caption,
                    imageUri = selectedImageUri.toString(),
                    profileUri = "android.resource://com.farhan164.post5pmob/${R.drawable.avatars_male}"
                )
                postViewModel.insert(newPost)
                Toast.makeText(this, "Postingan ditambahkan", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss() // Tutup dialog setelah disimpan
        }

        dialog.show()
    }

    private fun openGallery() {
        // ... (Fungsi ini tidak berubah)
        pickImageLauncher.launch("image/*")
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogPreviewImage = null
        dialogPreviewText = null
    }
}