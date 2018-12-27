package com.andresoller.domain.entities

class PostDetailsInfo(val title: String = "",
                      val body: String = "",
                      val name: String = "",
                      val commentsCount: Int = 0,
                      val comments: ArrayList<CommentInfo> = ArrayList())