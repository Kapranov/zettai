package sample

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
  val server = embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
    routing {
      get("/") {
        call.respondText("Hello, Ktor!", ContentType.Text.Plain)
      }
    }
  }
  server.start(wait = true)
}
