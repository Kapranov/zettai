package sample

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test as test
import sample.plugins.*

class SampleTest {
  @test
  fun helloWorldTest() = testApplication {
    application {
      configureRouting()
    }
    environment {
      config = MapApplicationConfig(
          "ktor.deployment.host" to "127.0.0.1",
          "ktor.deployment.port" to "8081",
          "ktor.deployment.shutdownTimeout" to "5000",
          "ktor.deployment.sslPort" to "8443",
          "ktor.environment" to "test"
      )
    }
    val response = client.get("/sample")
    val body = response.bodyAsText()
    assertEquals(HttpStatusCode.OK, response.status)
    assertEquals("Aloha Sample World! - HTTP/1.1", body)
    assertContains("Aloha Sample World! - HTTP/1.1", body)
  }
  @test
  fun respondsWithSampleWorldString(): Unit = runBlocking {
    val response: String = HttpClient().get("http://127.0.0.1:8080/sample").body()
    assertEquals("Aloha Sample World! - HTTP/1.1", response)
  }
}
