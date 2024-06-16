package http.stories

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test as test
import http.stories.plugins.*

class SeeTodoListTest {
  @test
  fun storiesTest() = testApplication {
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
    val response = client.get("/stories")
    val body = response.bodyAsText()
    assertEquals(HttpStatusCode.OK, response.status)
    assertEquals("Aloha Stories World!", body)
    assertContains("Aloha Stories World!", body)
  }
}
