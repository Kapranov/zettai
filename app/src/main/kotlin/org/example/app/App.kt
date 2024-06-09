package org.example.app

import org.example.utilities.StringUtils
import org.apache.commons.text.WordUtils

class App {
  val greeting: String
    get() {
      return "Hello, World!"
    }
}

fun main() {
  val tokens = StringUtils.split(MessageUtils.getMessage())
  val result = StringUtils.join(tokens)
  println(WordUtils.capitalize(result))
}
