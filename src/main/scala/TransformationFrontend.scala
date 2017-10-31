
import akka.actor.{Actor, ActorRef, Terminated}

class TransformationFrontend extends Actor {

  var backends: IndexedSeq[ActorRef] = IndexedSeq.empty[ActorRef]
  var jobCounter: Long = 0

  override def receive: Receive = {

    case job: TransformationJob if backends.isEmpty =>
      sender() ! JobFailed("Service Unavailable, please try later", job)

    case job: TransformationJob =>
      jobCounter += 1
      backends((jobCounter % backends.size).toInt) forward job

    case BackendRegistration if(!backends.contains(sender())) =>
      context watch sender()
      backends = backends :+ sender()

    case Terminated(a) =>
      backends = backends filterNot(_ == a)
  }

}
