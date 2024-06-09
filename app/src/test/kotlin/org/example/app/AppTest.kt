package org.example.app

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class AppTest {
  @Test
  fun appHasAGreeting() {
    val classUnderTest = main()
    assertNotNull(classUnderTest, "Hello World!")
  }
}