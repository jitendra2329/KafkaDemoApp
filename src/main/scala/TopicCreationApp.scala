import org.apache.kafka.clients.admin._

import java.util.Properties
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

object TopicCreationApp extends App {

  object Topics {
    final val OrdersByUserTopic = "orders-by-user"
    final val DiscountProfilesByUserTopic = "discount-profiles-by-user"
    final val DiscountsTopic = "discounts"
    final val PaymentsTopic = "payments"
  }

  private val bootstrapServers = "localhost:9092"
  private val adminProps = new Properties()
  adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

  private val adminClient = AdminClient.create(adminProps)

  import Topics._
  private val newTopics = Set(
    new NewTopic(OrdersByUserTopic, 1, 1.toShort),
    new NewTopic(DiscountsTopic, 1, 1.toShort),
    new NewTopic(DiscountProfilesByUserTopic, 1, 1.toShort),
    new NewTopic(PaymentsTopic, 1, 1.toShort)
  )

  Try(adminClient.createTopics(newTopics.asJavaCollection)) match {
    case Failure(_) => println("Topics could not create")
    case Success(value) => println("Topics created : "+ value.values().keySet())
  }
  adminClient.close()
}
