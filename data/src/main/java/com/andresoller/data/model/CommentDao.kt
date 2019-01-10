package com.andresoller.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments ORDER BY id")
    fun getComments(): Maybe<List<Comment>>

    @Query("SELECT * FROM comments WHERE postId = :postId")
    fun getPostComments(postId: String): Maybe<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(comments: List<Comment>)
}