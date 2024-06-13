package hello

import kotlin.io.println as println1

private fun getGreeting(): String {
  return "Hello Functional World!"
}

fun main() {
  println1(message = "Proto Polynesian Greeting")
  getGreeting().apply {
    print(this)
  }
}

class Tahitian {
  fun greeting(): String {
    return "Hello Functional World!"
  }
}