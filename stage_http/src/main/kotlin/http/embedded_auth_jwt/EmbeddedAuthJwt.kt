package http.embedded_auth_jwt

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import kotlinx.serialization.*

@Serializable
data class User(val username: String, val password: String)

fun main() {
  embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
    install(ContentNegotiation) {
      json()
    }
    val secret = "secret"
    val issuer =  "http://127.0.0.1:8080/"
    val audience = "http://127.0.0.1:8080/hello"
    val myRealm = "Access to 'hello'"
    install(Authentication) {
      jwt("auth-jwt") {
        realm = myRealm
        verifier(JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build())
        validate { credential ->
          if (credential.payload.getClaim("username").asString() != "") {
            JWTPrincipal(credential.payload)
          } else {
            null
          }
        }
        challenge { _, _ ->
          call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
        }
      }
    }
    routing {
      post("/login") {
        val user = call.receive<User>()
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 60_000))
            .sign(Algorithm.HMAC256(secret))
        call.respond(hashMapOf("token" to token))
      }

      authenticate("auth-jwt") {
        get("/hello") {
          val principal = call.principal<JWTPrincipal>()
          val username = principal!!.payload.getClaim("username").asString()
          val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
          call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
      }
    }
  }.start(wait = true)
}
