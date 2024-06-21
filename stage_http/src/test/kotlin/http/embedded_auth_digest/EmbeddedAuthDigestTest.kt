package http.embedded_auth_digest

import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*

const val myRealm = "Access to the '/' path"
const val myAlgorithm = "SHA-256"

class EmbeddedAuthDigestTest {
  @Test
  fun testAuthRoute() = testApplication {
    val client = createClient {
      install(Auth) {
        digest {
          algorithmName = myAlgorithm
          credentials {
            DigestAuthCredentials(username = "jetbrains", password = "foobar")
          }
          realm = myRealm
        }
      }
    }
    client.get("/").bodyAsText()
  }
}
