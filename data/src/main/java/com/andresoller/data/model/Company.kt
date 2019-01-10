package com.andresoller.data.model

import androidx.room.ColumnInfo

data class Company(val bs: String = "",
                   val catchPhrase: String = "",
                   @ColumnInfo(name = "companyName")
                   val name: String = "")
