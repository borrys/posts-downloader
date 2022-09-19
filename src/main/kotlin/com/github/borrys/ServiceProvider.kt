package com.github.borrys

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun interface Serializer {
  fun serialize(item: Post): String
}

fun interface FileSaver {
  fun save(filename: String, body: String): Unit
}

class ServiceProvider(configuration: Configuration) {
  private val gson = Gson()

  private val httpClient = OkHttpClient()

  private val retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(configuration.baseUrl)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

  private val fileSaver = LocalDirectoryFileSaver(configuration.destinationDirectory)

  private val postRepository: PostRepository = retrofit.create(PostRepository::class.java)

  private val serializer = Serializer { gson.toJson(it) }

  val downloader = Downloader(postRepository, serializer, fileSaver)

  fun shutdown() {
    httpClient.dispatcher().executorService().shutdown()
    httpClient.connectionPool().evictAll()
  }
}