package com.github.borrys

import retrofit2.http.GET

interface PostRepository {
  @GET("/posts")
  suspend fun getAllPosts(): List<Post>
}

data class Post(
  val id: Long,
  val userId: Long,
  val title: String,
  val body: String
)