package http.embedded_auth_bearer

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
  embeddedServer(Netty, port = 8080) {
    install(Authentication) {
      bearer("auth-bearer") {
        realm = "Access to the '/' path"
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
      authenticate("auth-bearer") {
        get("/") {
          call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
        }
      }
    }
  }.start(wait = true)
}
