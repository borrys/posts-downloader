package com.github.borrys

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File

val defaultConfiguration =
  Configuration(
    baseUrl = ("https://jsonplaceholder.typicode.com/"),
    destinationDirectory = File("./output")
  )

fun main() {
  val serviceProvider = ServiceProvider(defaultConfiguration)
  val downloader = serviceProvider.downloader

  val result = runBlocking(Dispatchers.IO) { downloader.process() }

  println(
    when (result) {
      is Success -> "Successfully saved ${result.savedFiles} files"
      is Failure -> "Failed to process post due to error:\n${result.details}"
    }
  )
  
  serviceProvider.shutdown()
}
