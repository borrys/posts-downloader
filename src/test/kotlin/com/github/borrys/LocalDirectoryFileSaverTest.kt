package com.github.borrys

import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LocalDirectoryFileSaverTest {

  @Test
  internal fun `writes file in directory`() {
    val directory = Files.createTempDirectory("file-saver-test").toFile()
    val fileSaver = LocalDirectoryFileSaver(directory)
    val filename = "test.txt"
    val body = "Lorem ipsum sit amet"

    fileSaver.save(filename, body)

    val destFile = File(directory, filename)
    assertTrue { destFile.exists() }
    assertEquals(body, destFile.readText())
  }

  @Test
  internal fun `when directory does not exists then it is created`() {
    val directory = Files.createTempDirectory("file-saver-test").toFile()
    val subDirectory = File(directory, "new-subdirectory")

    LocalDirectoryFileSaver(subDirectory)

    assertTrue { subDirectory.exists() }
  }
}