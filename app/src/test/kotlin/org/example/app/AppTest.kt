package org.example.app

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class AppTest {
  @Test
  fun appHasAMain() {
    val classUnderTest = main()
    assertNotNull(classUnderTest, "Hello World!")
  }
  @Test
  fun appHasAGreeting() {
    val classUnderTest = App()
    assertNotNull(classUnderTest.greeting, "app should have a greeting")
  }
  @Test
  fun appGetGreeting() {
    greeting().also {
      assertNotNull(it, "app should have a greeting")
    }
  }
}
