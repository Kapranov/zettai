package http.embedded_auth_bearer

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EmbeddedAuthBearerTest {
  @Test
  fun testAuthRoute() = testApplication {
    application {
      install(Authentication) {
        bearer {
          authenticate { tokenCredential ->
            if (tokenCredential.token == "abc123") {
              UserIdPrincipal("jetbrains")
            } else {
              null
            }
          }
        }
      }
      routing {
        authenticate {
          get("/") {
            call.respondText("Hello, jetbrains!")
          }
        }
      }
    }
    val response = client.get("/") {
      header(HttpHeaders.Authorization, "Bearer abc123")
    }
    assertEquals("Hello, jetbrains!", response.bodyAsText())
  }
}
