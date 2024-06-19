package http.embedded_auth_basic_hashed

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EmbeddedAuthBasiciHashedTest {
  @Test
  fun testAuthRoute() = testApplication {
    application {
      install(Authentication) {
        basic {
          validate {
            val credentials = Base64.getEncoder().encodeToString("admin:password".toByteArray())
            UserIdPrincipal(credentials)
          }
        }
      }
      routing {
        authenticate {
          get("/") {
            call.respondText { "Hello, admin!" }
          }
        }
      }
    }
    val client = createClient {
      defaultRequest {
        val credentials = Base64.getEncoder().encodeToString("admin:password".toByteArray())
        header(HttpHeaders.Authorization, "Basic $credentials")
      }
    }
    val response = client.get("/")
    assertEquals("Hello, admin!", response.bodyAsText())
  }
}
