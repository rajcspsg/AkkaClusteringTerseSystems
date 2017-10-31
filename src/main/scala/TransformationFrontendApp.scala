import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.duration._

object TransformationFrontendApp extends App {

  val port = if (args.isEmpty) "0" else args(0)
  val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]")).
    withFallback(ConfigFactory.load())

  val system = ActorSystem("ClusterSystem", config)
  val frontend = system.actorOf(Props[TransformationFrontend], name = "frontend")

  val counter = new AtomicInteger
  import system.dispatcher
  system.scheduler.schedule(2.seconds, 2.seconds) {
    implicit val timeout = Timeout(5 seconds)
    (frontend ? TransformationJob("hello-" + counter.incrementAndGet())) onSuccess {
      case result => println(result)
    }
  }

  Thread.sleep(100000)
}
