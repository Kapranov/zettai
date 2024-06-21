package http.embedded_auth_digest

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.Principal
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.*
import kotlin.text.Charsets.UTF_8

fun getMd5Digest(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))

const val myRealm = "Access to the '/' path"
val userTable: Map<String, ByteArray> = mapOf(
    "jetbrains" to getMd5Digest("jetbrains:$myRealm:foobar"),
    "admin" to getMd5Digest("admin:$myRealm:password")
)

fun main() {
  embeddedServer(Netty, port = 8080) {
    install(Authentication) {
      digest("auth-digest") {
        realm = myRealm
        digestProvider { userName, _: String ->
          userTable[userName]
        }
        validate { credentials ->
          if (credentials.userName.isNotEmpty()) {
            CustomPrincipal(credentials.userName, credentials.realm)
          } else {
            null
          }
        }
      }
    }
    routing {
      authenticate("auth-digest") {
        get("/") {
          call.respondText("Hello, ${call.principal<CustomPrincipal>()?.userName}!")
        }
      }
    }
  }.start(wait = true)
}

data class CustomPrincipal(val userName: String, val realm: String) : Principal
