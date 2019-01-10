package com.andresoller.data.model

import androidx.room.Embedded

data class Address(val zipcode: String = "",
                   @Embedded
                   val geo: Geo = Geo(),
                   val suite: String = "",
                   val city: String = "",
                   val street: String = "")
