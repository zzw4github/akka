package me.zzw.app.actors


import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by infosea on 2016-09-27.
  */
object DemoActor {
  def props(magicNumber: Int):Props = Props(
    new DemoActor(magicNumber))
  }

class DemoActor(magicNumber: Int) extends Actor{
  def receive = {
      case x: Int => sender() ! (x + magicNumber)
  }
}

class SomeOtherActor extends Actor {
  import scala.concurrent.duration._
//  context.actorOf(DemoActor.props(11), "demo")
  def receive = {
    case x: Int => {
      println(x)
      context.setReceiveTimeout(100 milliseconds)
      context stop sender()
      context stop self
    }
  }
}

object App1 extends App {
  val system = ActorSystem("app")
  val demo = system.actorOf(DemoActor.props(1),"demo")
  val otherDemo = system.actorOf(Props[SomeOtherActor],"otherDemo")
  demo.tell(1, otherDemo)
}
