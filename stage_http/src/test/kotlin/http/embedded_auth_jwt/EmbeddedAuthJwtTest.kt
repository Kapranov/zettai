package http.embedded_auth_jwt

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.connector
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import java.util.*
import kotlin.test.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class EmbeddedAuthJwtTest {
  @Test
  fun testHello() = testApplication {
    val secret = "secret"
    val issuer =  "http://localhost:8080/"
    val audience = "http://localhost:8080/hello"
    val myRealm = "Access to 'hello'"

    environment {
      config = MapApplicationConfig(listOf("ktor.deployment.host" to "localhost"))
      connector {
        host = "localhost"
        port = 8080
      }
    }

    application {
      install(Authentication) {
        jwt("auth-jwt") {
          realm = myRealm
          verifier(JWT
              .require(Algorithm.HMAC256(secret))
              .withAudience(audience)
              .withIssuer(issuer)
              .build())
          validate { credential ->
            JWTPrincipal(credential.payload)
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
    }

    val client = createClient {
      install(ContentNegotiation) {
        json(Json {
          prettyPrint = true
          isLenient = true
        })
      }
    }

    val token = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", "jetbrains")
        .withExpiresAt(Date(System.currentTimeMillis() + 60_000))
        .sign(Algorithm.HMAC256(secret))

    val tokenInvalid = client.get("http://localhost:8080/hello") {
      header(HttpHeaders.Authorization, "Bearer token_invalid")
    }
    assertEquals("Token is not valid or has expired", tokenInvalid.bodyAsText())

    val tokenValid = client.get("http://localhost:8080/hello") {
      header(HttpHeaders.Authorization, "Bearer $token")
    }.bodyAsText()
    assertTrue {
      tokenValid.contains("Hello, jetbrains!")
    }

    val jsonData = Json.encodeToString(AuthToken.serializer(), AuthToken(token))
    println(jsonData)
  }
}

@Serializable
data class User(val username: String, val password: String)

@Serializable
data class AuthToken(val token: String)
