package pukui

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test as test

class PukkaTest {
  @test
  fun simpleTest() {
    val greeting = Tuvaluan().getGreeting()
    assertEquals(greeting, "Hello Functional World!")
  }
}
