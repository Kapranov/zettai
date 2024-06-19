package http.embedded_auth_basic_hashed

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
val hashedUserTable = UserHashedTableAuth(
    table = mapOf(
        "jetbrains" to digestFunction("foobar"),
        "admin" to digestFunction("password")
    ),
    digester = digestFunction
)

fun main() {
  embeddedServer(Netty, port = 8080) {
    install(Authentication) {
      basic("auth-basic-hashed") {
        realm = "Access to the '/' path"
        validate { credentials ->
          hashedUserTable.authenticate(credentials)
        }
      }
    }
    routing {
      authenticate("auth-basic-hashed") {
        get("/") {
          call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
        }
      }
    }
  }.start(wait = true)
}
