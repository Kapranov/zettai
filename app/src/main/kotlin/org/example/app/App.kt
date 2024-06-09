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

fun greeting() {
  val firstName = "Adam"
  val middle = 'c'
  val lastName = "Brown"
  val age = 15
  val name : String = firstName
  val story = App().greeting
  println("\n$name $middle $lastName $age - $story\n")
  greet("Aloha!")
  greet(englishGreeting())
  greet(italianGreeting())
}

fun greet(msg: String) {
  println(msg)
}

fun englishGreeting() : String = "Hello, World!"

fun italianGreeting() : String {
  return "Bon Giordano!"
}