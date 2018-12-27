package com.andresoller.data.model

data class User(val website: String = "",
                val address: Address = Address(),
                val phone: String = "",
                val name: String = "",
                val company: Company = Company(),
                val id: Int = -1,
                val email: String = "",
                val username: String = "")
