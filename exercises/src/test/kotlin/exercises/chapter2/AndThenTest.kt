package exercises.chapter2

import java.net.URI
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo

fun fetchListV1(request: Request): Response =
    createResponse(
        renderHtml(
            fetchListContent(
                extractListData(
                    request
                )
            )
        )
    )

fun fetchListV2(request: Request): Response =
    request.let(::extractListData)
        .let(::fetchListContent)
        .let(::renderHtml)
        .let(::createResponse)

val fetchListV3 = ::extractListData andThen
        ::fetchListContent andThen
        ::renderHtml andThen
        ::createResponse

class AndThenTest {
  @Test
  fun `concat many functions for version one`() {
    val req = Request("GET", URI("http://example.com/zettai/uberto/mylist"), "")
    val resp = fetchListV1(req)
    println(resp)
    expectThat(resp) {
      get { status }.isEqualTo(200)
      get { this.body }.contains("<td>uberto buy milk</td>")
      get { this.body }.contains("<td>complete mylist</td>")
      get { this.body }.contains("<td>something else</td>")
    }
  }
  @Test
  fun `concat many functions for version two`() {
    val req = Request("GET", URI("http://example.com/zettai/uberto/mylist"), "")
    val resp = fetchListV2(req)
    println(resp)
    expectThat(resp) {
      get { status }.isEqualTo(200)
      get { this.body }.contains("<td>uberto buy milk</td>")
      get { this.body }.contains("<td>complete mylist</td>")
      get { this.body }.contains("<td>something else</td>")
    }
  }
  @Test
  fun `concat many functions for version three`() {
    val req = Request("GET", URI("http://example.com/zettai/uberto/mylist"), "")
    val resp = fetchListV3(req)
    println(resp)
    expectThat(resp) {
      get { status }.isEqualTo(200)
      get { this.body }.contains("<td>uberto buy milk</td>")
      get { this.body }.contains("<td>complete mylist</td>")
      get { this.body }.contains("<td>something else</td>")
    }
  }
}
