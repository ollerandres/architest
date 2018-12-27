package com.andresoller.data.model

data class Address(val zipcode: String = "",
                   val geo: Geo = Geo(),
                   val suite: String = "",
                   val city: String = "",
                   val street: String = "")
