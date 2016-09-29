package me.zzw.app.actors.become

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by infosea on 2016-09-27.
  */
class HotSwapActor extends Actor{
  import context._
  def angry: Receive = {
    case "foo" => sender() ! "I am already angry?"
    case "bar" => become(happy)
  }

  def happy: Receive = {
    case "bar" => sender() ! "I am already happy :="
    case "foo" => become(angry)
  }
  def receive = {
    case "foo" => become(angry)
    case "bar" => become(happy)
  }

}

object HotSwapActor extends App{
  val system = ActorSystem("system")
  val actor = system.actorOf(Props[HotSwapActor], "hotSwapActor")
  actor.tell("foo",actor)
}
