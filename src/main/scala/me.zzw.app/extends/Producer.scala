package me.zzw.app.extend

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.stream.stage.GraphStageLogic.StageActorRef.Receive

trait ProducerBehavior {
  this: Actor =>
  val producerBehavior: Receive = {
    case GiveMeThings =>
      sender() ! Give("thing")
  }
}

trait ConsumerBehavior {
  this: Actor with ActorLogging =>

  val consumerBehavior: Receive = {
    case ref: ActorRef =>
      ref ! GiveMeThings
    case Give(thing) =>
      log.info("Got a thing ! It' {}", thing)
  }
}

/**
  * Created by infosea on 2016-09-29.
  */
class Producer extends Actor with ActorLogging with ProducerBehavior{
  def receive = producerBehavior
}

class Consumer extends Actor with ActorLogging with ConsumerBehavior {
  def receive = consumerBehavior
}

class ProducerConsumer extends Actor with ActorLogging with ProducerBehavior with ConsumerBehavior {
  def receive = producerBehavior.orElse[Any, Unit](consumerBehavior)
}

// protocol
case object GiveMeThings
final case class Give(thing: Any)