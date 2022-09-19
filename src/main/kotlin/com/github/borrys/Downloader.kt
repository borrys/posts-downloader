package com.github.borrys

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

sealed interface Result
data class Success(val savedFiles: Int) : Result
data class Failure(val details: Exception) : Result

class Downloader(
  private val postRepository: PostRepository,
  private val serializer: Serializer,
  private val fileSaver: FileSaver
) {
  suspend fun process() = try {
    val posts = postRepository.getAllPosts()
    saveAll(posts)
    Success(posts.size)
  } catch (exception: Exception) {
    Failure(exception)
  }

  private suspend fun saveAll(posts: List<Post>) = coroutineScope {
    posts
      .map { it.id to serializer.serialize(it) }
      .forEach { (id, content) ->
        launch { fileSaver.save("$id.json", content) }
      }
  }
}