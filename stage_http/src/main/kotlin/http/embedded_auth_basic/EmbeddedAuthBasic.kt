package http.embedded_auth_basic

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
  embeddedServer(Netty, port = 8080) {
    install(Authentication) {
      basic("auth-basic") {
        realm = "Access to the '/' path"
        validate { credentials ->
          if (credentials.name == "jetbrains" && credentials.password == "foobar") {
            UserIdPrincipal(credentials.name)
          } else {
            null
          }
        }
      }
    }
    routing {
      authenticate("auth-basic") {
        get("/") {
          call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
        }
      }
    }
  }.start(wait = true)
}
