package hello

import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test as test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class HelloTest {
  private fun randomNatural() = Random.nextInt(from = 1, until = 100_000_000)
  @test
  fun simpleTest() {
    val greeting = Tahitian().greeting()
    assertEquals(greeting, "Hello Functional World!")
  }
  @test
  fun simplest() {
    assertEquals(
      2,
      1 + 1,
      "1 + 1 should equal 2"
    )
  }
  @test
  fun `add two numbers`() {
    val num1 = 5 + 6
    val num2 = 7 + 42
    val num3 = 9_999 + 1
    expectThat(num1).isEqualTo(11)
    expectThat(num2).isEqualTo(49)
    expectThat(num3).isEqualTo(10_000)
  }
  @test
  fun `zero identity` () {
    repeat(100) {
      val x = randomNatural()
      expectThat(x + 0).isEqualTo(x)
    }
  }
  @test
  fun `commutative property` () {
    repeat(100) {
      val x = randomNatural()
      val y = randomNatural()
      expectThat(x + y).isEqualTo(y + x)
    }
  }
  @test
  fun `associative property` () {
    repeat(100) {
      val x = randomNatural()
      val y = randomNatural()
      val z = randomNatural()
      expect {
        that((x + y) + z).isEqualTo(x + (y + z))
        that((y + z) + x).isEqualTo(y + (z + x))
        that((z + x) + y).isEqualTo(z + (x + y))
      }
    }
  }
  @test
  fun `adding Y is equal to adding one Y times`() {
    repeat(100) {
      val x = randomNatural()
      val y = Random.nextInt(1, 100)
      val ones = List(y) { 1 }
      val z = ones.fold(x) { acc, one -> acc + one }
      expectThat(z).isEqualTo(x + y)
    }
  }
}
