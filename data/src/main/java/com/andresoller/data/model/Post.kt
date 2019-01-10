package com.andresoller.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
        @PrimaryKey @ColumnInfo(name = "id") val id: Int = -1,
        val title: String = "",
        val body: String = "",
        val userId: Int = -1)
