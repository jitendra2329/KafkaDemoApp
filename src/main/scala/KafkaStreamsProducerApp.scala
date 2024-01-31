import KafkaStreamsApp.Topics._
import org.apache.kafka.streams.StreamsConfig

import java.util.concurrent.Future
import scala.util.Random

object KafkaStreamsProducerApp extends App {

  import org.apache.kafka.clients.producer._

  import java.util.Properties

  val  props = new Properties()
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  private def produceDiscount: Future[RecordMetadata] = {

    val profile1 = new ProducerRecord(DiscountsTopic, "profile1",
      s"""{
         |"profile":"profile1",
         |"amount":0.5
         |}""".stripMargin)

    val profile2 = new ProducerRecord(DiscountsTopic, "profile2",
      s"""{
         |"profile":"profile1",
         |"amount":0.25
         |}""".stripMargin)

    val profile3 = new ProducerRecord(DiscountsTopic, "profile3",
      s"""{
         |"profile":"profile3",
         |"amount":0.15
         |}""".stripMargin)

    println(s"producing to $DiscountsTopic")
    producer.send(profile1)
    producer.send(profile2)
    producer.send(profile3)
  }

  private def produceDiscountProfilesByUser: Future[RecordMetadata] = {
    println(s"producing to $DiscountProfilesByUserTopic")
    val discount1=new ProducerRecord(DiscountProfilesByUserTopic, "Jitendra","profile1")
    producer.send(discount1)
    val discount2=new ProducerRecord(DiscountProfilesByUserTopic, "Akhil","profile2")
    producer.send(discount2)

  }

  def uuid = java.util.UUID.randomUUID.toString

  val order1ID= s"order$uuid"
  val order2ID= s"order$uuid"

 private def produceOrderByUser: Future[RecordMetadata] =  {
    val order1 = new ProducerRecord(OrdersByUserTopic, "Jitendra Kumar",
      s"""{"orderId":"$order1ID","user":"Jitendra Kumar","products":[ "iPhone 133","MacBook Pro 19","${Random.alphanumeric.take(10).mkString}"],"amount":4200.0 }""")

    val order2 = new ProducerRecord(OrdersByUserTopic, "Akhil Trivedi",
      s"""{"orderId":"$order2ID","user":"Akhil Trivedi","products":["iPhone 111","${Random.alphanumeric.take(5).mkString}"],"amount":804.0}""")

   println(s"producing to $OrdersByUserTopic")
    producer.send(order1)
    producer.send(order2)
  }

  private def producePayments: Future[RecordMetadata] = {
    val payment1 = new ProducerRecord(PaymentsTopic, "order1",
      s"""{"orderId":"$order1ID","status":"PAID"}""")

    val payment2 = new ProducerRecord(PaymentsTopic, "order2",
      s"""{"orderId":"$order2ID","status":"PENDING"}""")

    println(s"producing to $PaymentsTopic")
    producer.send(payment1)
    producer.send(payment2)
  }

  println("Choose topic to produce in: ")
  println("1. orders-by-user")
  println("2. discounts")
  println("3. discount-profiles-by-user")
  println("4. payments")

  private val topic = scala.io.StdIn.readInt()

  topic match {
    case 1 => produceOrderByUser
    case 2 => produceDiscount
    case 3 => produceDiscountProfilesByUser
    case 4 => producePayments
    case _ => println("*********** Topic does not exist **************")
  }

  producer.close()
}
