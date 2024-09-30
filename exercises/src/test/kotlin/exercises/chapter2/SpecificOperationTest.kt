package exercises.chapter2

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

data class Product(val name: String, val price: Double)

class SpecificOperationTest {
  @Test
  fun `retrieve elements by index`() {
    val numbers = listOf(1, 2, 3, 4)
    val number = numbers[0]
    println(message = numbers.indexOf(2))
    println(numbers.lastIndexOf(2))
    println( numbers[0])
    expectThat(number).isEqualTo(1)
  }
  @Test
  fun `retrieve list parts`() {
    val numbers = (0..13).toList()
    println(numbers.subList(3, 6))
    expectThat(numbers.subList(3, 6)).isEqualTo(listOf(3, 4, 5))
  }
  @Test
  fun `binary search in sorted`() {
    val numbers = mutableListOf("one", "two", "three")
    numbers.sort()
    println(numbers.binarySearch("two"))
    expectThat(numbers.binarySearch("two")).isEqualTo(2)
  }
  @Test
  fun `comparator binary`() {
    val productList = listOf(
        Product("WebStorm", 49.0),
        Product("AppCode", 99.0),
        Product("DotTrace", 129.0),
        Product("ReSharper", 149.0)
    )
    val value1 = productList.binarySearch(Product("AppCode", 99.0), compareBy<Product> { it.price }.thenBy { it.name })
    println(value1)
    expectThat(value1).isEqualTo(1)

    val colors = listOf("Blue", "green", "ORANGE", "Red", "yellow")
    val value2 = colors.binarySearch("RED", String.CASE_INSENSITIVE_ORDER)
    println(value2)
    expectThat(value2).isEqualTo(3)
  }
  @Test
  fun `comparison binary`() {
    fun priceComparison(product: Product, price: Double) = (product.price - price).toInt()
    val productList = listOf(
        Product("WebStorm", 49.0),
        Product("AppCode", 99.0),
        Product("DotTrace", 129.0),
        Product("ReSharper", 149.0)
    )

    val value = productList.binarySearch {priceComparison(it , 99.0)}
    println(value)
    expectThat(value).isEqualTo(1)
  }
}
