package com.andresoller.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id")
    fun getUsers(): Maybe<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Maybe<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<User>)
}