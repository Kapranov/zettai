package http.stories.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
  routing {
    get("/stories") {
      call.respondText("Aloha Stories World!")
    }
  }
}
