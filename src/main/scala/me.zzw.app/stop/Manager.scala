package me.zzw.app.stop

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props, Terminated}
import akka.pattern.gracefulStop

import scala.concurrent.Await

/**
  * Created by infosea on 2016-09-29.
  */
//try{
//val stopped: Future[Boolean] = gracefulStop(actorRef, 5 seconds, Manager.Shutdown)
//Await.result(stopped, 6 seconds)
//}catch{
//case e: akka.pattern.AskTimeoutException =>
//}
  object Manager {
    case object Shutdown

  def main(arg :Array[String]): Unit ={
    val manager = ActorSystem("Manager").actorOf(Props[Manager], "manager")
    manager.tell("job", ActorRef.noSender)
    manager.tell(Shutdown, manager)
  }
  }

class Cruncher extends Actor {
  def receive = {
    case "crunch" =>
      println("crunch")
      sender().tell("job", self)
    case _ => println("___")
  }
}

class Manager extends Actor{
  import Manager._
  val crunch = context.watch(context.actorOf(Props[Cruncher], "crunch"))

  def receive = {
    case "job" =>
      crunch ! "crunch"
      sender().tell("crunch", self)
    case Shutdown => crunch ! PoisonPill
      context become shuttingDown
  }

  def shuttingDown: Receive = {
    case "job" => sender() ! "service unavailable, shutting down"
    case Terminated(`crunch`) => context stop self
  }




}
