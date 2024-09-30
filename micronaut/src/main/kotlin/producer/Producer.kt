package producer

import com.github.javafaker.Faker
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

const val hostname: String = "localhost:9092"
const val topics: String = "persons"

val rate
  get() = 1000L

fun main() {
  SimpleProducer(hostname).produce(2)
}

class SimpleProducer(brokers: String) {
  fun produce(ratePerSecond: Int) {
    val personsTopic = topics
    val waitTimeBetweenIterationsMs = rate / ratePerSecond
    val faker = Faker()
    val jsonMapper = ObjectMapper().apply {
      registerKotlinModule()
      disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      dateFormat = StdDateFormat()
    }

    while (true) {
      val fakePerson = Person(
          firstName = faker.name().firstName(),
          lastName = faker.name().lastName(),
          birthDate = faker.date().birthday()
      )
      val fakePersonJson = jsonMapper.writeValueAsString(fakePerson)
      val futureResult = producer.send(ProducerRecord(personsTopic, fakePersonJson))
      Thread.sleep(waitTimeBetweenIterationsMs)
      futureResult.get()
    }
  }

  private val producer = createProducer(brokers)
  private fun createProducer(brokers: String): Producer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = brokers
    props["key.serializer"] = StringSerializer::class.java
    props["value.serializer"] = StringSerializer::class.java
    return KafkaProducer(props)
  }
}
