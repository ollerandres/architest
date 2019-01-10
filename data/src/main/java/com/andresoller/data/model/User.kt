package com.andresoller.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(@PrimaryKey @ColumnInfo(name = "id") val id: Int = -1,
                val website: String = "",
                @Embedded
                val address: Address = Address(),
                val phone: String = "",
                val name: String = "",
                @Embedded
                val company: Company = Company(),
                val email: String = "",
                val username: String = "")
