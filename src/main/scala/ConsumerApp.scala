
import KafkaStreamsApp.Topics._
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.streams.StreamsConfig

import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters._
import scala.jdk.DurationConverters._


object ConsumerApp extends App {

  import java.util.Properties

  println("Choose topic to consume from: ")
  println("1. orders-by-user")
  println("2. discounts")
  println("3. discount-profiles-by-user")
  println("4. payments")

  val TOPIC = scala.io.StdIn.readInt() match {
    case 1 => OrdersByUserTopic
    case 2 => DiscountsTopic
    case 3 => DiscountProfilesByUserTopic
    case 4 => PaymentsTopic
  }

  val props = new Properties()
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "something")

  private val consumer = new KafkaConsumer[String, String](props)

  consumer.subscribe(List(TOPIC).asJavaCollection)


  println(s"consuming $TOPIC")

  while (true) {
    val records: ConsumerRecords[String, String] = consumer.poll(10.seconds.toJava)
    for (record <- records.asScala) {
      println(record.value())
    }
  }

}
