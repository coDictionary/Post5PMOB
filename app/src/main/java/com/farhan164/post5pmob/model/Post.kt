package com.farhan164.post5pmob.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val caption: String,
    val imageUri: String,
    val profileUri: String
)