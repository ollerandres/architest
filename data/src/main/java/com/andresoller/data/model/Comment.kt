package com.andresoller.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(@PrimaryKey @ColumnInfo(name = "id") val id: Int = -1,
                   val name: String = "",
                   val postId: Int = -1,
                   val body: String = "",
                   val email: String = "")
