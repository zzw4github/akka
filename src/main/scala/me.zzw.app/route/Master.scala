package me.zzw.app.route

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

/**
  * Created by infosea on 2016-09-28.
  */
case class Work(name: String)

class Worker extends Actor {
  def receive ={
    case Work => println("this is works")
    case _ => println("none")
  }
}

class Master extends Actor {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case w: Work =>
      router.route(w, sender())
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Worker])
      context watch r
      router = router.addRoutee(r)
  }
}
  object Master {
  def main(arg: Array[String]): Unit ={
    val system = ActorSystem("master")

    // Create the 'greeter' actor
    val worker = system.actorOf(Props[Worker], "worker")
    worker.tell(Work,ActorRef.noSender)
  }
}

