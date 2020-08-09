package com.andresoller.domain.entities

data class UserInfo(val website: String = "",
                    val phone: String = "",
                    val name: String = "",
                    val id: Int = -1,
                    val email: String = "",
                    val username: String = "")