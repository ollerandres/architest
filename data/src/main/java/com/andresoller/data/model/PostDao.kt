package com.andresoller.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id")
    fun getPosts(): Maybe<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPost(postId: String): Maybe<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)
}