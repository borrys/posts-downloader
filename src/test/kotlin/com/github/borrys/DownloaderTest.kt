package com.github.borrys

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

internal class DownloaderTest {
  @MockK
  lateinit var postRepository: PostRepository

  @MockK
  lateinit var fileSaver: FileSaver

  @SpyK
  var serializer = TestSerializer()

  @InjectMockKs
  lateinit var downloader: Downloader

  @BeforeEach
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

  @Test
  internal fun `saves serialized posts`() = runBlocking {
    coEvery { postRepository.getAllPosts() } returns listOf(
      Post(1, 42, "foo", "Lorem ipsum"), Post(2, 21, "bar", "dolor sit amet")
    )

    downloader.process()

    verifyAll {
      fileSaver.save("1.json", "42 foo Lorem ipsum")
      fileSaver.save("2.json", "21 bar dolor sit amet")
    }
  }

  @Test
  internal fun `returns number of saved posts`() = runBlocking {
    coEvery { postRepository.getAllPosts() } returns listOf(
      Post(1, 42, "foo", "Lorem ipsum"),
      Post(2, 21, "bar", "dolor sit amet"),
      Post(3, 42, "baz", "consectetur adipiscing elit")
    )

    val result = downloader.process()

    assertEquals(Success(3), result)
  }

  @Test
  internal fun `when empty post list returned then does not save anything`() = runBlocking {
    coEvery { postRepository.getAllPosts() } returns emptyList()

    val result = downloader.process()

    assertEquals(Success(0), result)
    verify(inverse = true) { fileSaver.save(any(), any()) }
  }

  @Test
  internal fun `when fetching posts fails then error is returned`() = runBlocking {
    val httpException = HttpException(Response.error<Any>(500, mockk(relaxed = true)))
    coEvery { postRepository.getAllPosts() } throws httpException

    val result = downloader.process()

    assertEquals(Failure(httpException), result)
  }
}

class TestSerializer : Serializer {
  override fun serialize(item: Post) = "${item.userId} ${item.title} ${item.body}"
}