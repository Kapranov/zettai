package sample.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
  routing {
    get("/sample") {
      call.respondText("Aloha Sample World!")
    }
  }
}
