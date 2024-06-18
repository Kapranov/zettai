package http.embedded_modules

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ModulesTest {
  @Test
  fun testModule1() = testApplication {
    application {
      module1()
      module2()
    }

    val response1 = client.get("/module1")
    assertEquals(HttpStatusCode.OK, response1.status)
    assertEquals("Hello from 'module1'!", response1.bodyAsText())

    val response2 = client.get("/module2")
    assertEquals(HttpStatusCode.OK, response2.status)
    assertEquals("Hello from 'module2'!", response2.bodyAsText())
  }
}
