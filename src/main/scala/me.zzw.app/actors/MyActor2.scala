package me.zzw.app.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by infosea on 2016-09-27.
  */
object MyActor2 {
  case class Greeting(from: String)
  case object Goodbye

  def main( args: Array[String]): Unit ={
    import akka.actor.ActorSystem
    val system = ActorSystem("mySys")
    val myActor2 = system.actorOf(Props[MyActor2], "myActor2")
    myActor2.tell(Greeting("zzw"),ActorRef.noSender)
  }
}

class MyActor2 extends Actor with ActorLogging {
  import MyActor2._
  def receive = {
    case Greeting(greeter) => log.info(s"I was greeted by $greeter.")
    case Goodbye => log.info("Someone said goodbye to me.")
  }


}
