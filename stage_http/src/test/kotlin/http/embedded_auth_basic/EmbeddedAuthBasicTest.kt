package http.embedded_auth_basic

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EmbeddedAuthBasicTest {
  @Test
  fun testAuthRoute() = testApplication {
    application {
      install(Authentication) {
        basic {
          validate {
            if (it.name == "jetbrains" && it.password == "foobar") UserIdPrincipal(it.name) else null
          }
        }
      }
      routing {
        authenticate {
          get("/") {
            call.respondText { "Hello, jetbrains!" }
          }
        }
      }
    }
    val response = client.get("/") {
      basicAuth("jetbrains", "foobar")
    }

    assertEquals(HttpStatusCode.OK, response.status)
    assertEquals("Hello, jetbrains!", response.bodyAsText())
  }
}
