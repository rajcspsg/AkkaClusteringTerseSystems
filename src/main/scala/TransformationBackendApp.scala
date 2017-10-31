import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object TransformationBackendApp extends App {

  val port = if (args.isEmpty) "0" else args(0)
  val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]")).
    withFallback(ConfigFactory.load())

  val system = ActorSystem("ClusterSystem", config)
  system.actorOf(Props[TransformationBackend], name = "backend")

}
