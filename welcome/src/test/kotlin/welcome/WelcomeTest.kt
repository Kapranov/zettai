package welcome

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test as test

class WelcomeTest {
  @test
  fun simpleTest() {
    val greeting =  Samoan().getGreeting()
    assertEquals(greeting, "Hello Functional World!")
  }
}

class WelcomeConsole {
  fun print(text: String) {
    println(text)
  }
}

class WelcomeWorld {
  val tesxt: String = "Hello World!"
}
