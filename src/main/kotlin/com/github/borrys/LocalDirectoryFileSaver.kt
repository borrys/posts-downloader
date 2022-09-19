package com.github.borrys

import java.io.File

class LocalDirectoryFileSaver(private val directory: File) : FileSaver {
  init {
    if (!directory.exists()) {
      directory.mkdirs()
    }
  }

  override fun save(filename: String, body: String) {
    File(directory, filename).writeText(body)
  }
}