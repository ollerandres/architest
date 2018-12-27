package com.andresoller.data.model

data class Comment(val name: String = "",
                   val postId: Int = -1,
                   val id: Int = -1,
                   val body: String = "",
                   val email: String = "")
