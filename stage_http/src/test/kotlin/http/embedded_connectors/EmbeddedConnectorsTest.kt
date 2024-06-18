package http.embedded_connectors

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*

class ConnectorsTest {
  @Test
  fun testApi(): Unit = testApplication {
    environment {
      envConfig()
    }
    client.get("/") {
      host = "0.0.0.0"
      port = 8080
    }.apply {
      assertEquals("Connected to public API", bodyAsText())
    }
    client.get("/") {
      host = "127.0.0.1"
      port = 8081
    }.apply {
      assertEquals("Connected to private API", bodyAsText())
    }
  }
}
