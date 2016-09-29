package me.zzw.app.actors.api

import akka.actor.{Actor, Props, Terminated}

/**
  * Created by infosea on 2016-09-29.
  */
class WatchActor extends Actor{
  val child = context.actorOf(Props.empty)
  context.watch(child)
  // <-- this is the only call needed fo registration
  var lastSender = context.system.deadLetters

  def receive: Receive = {
    case "kill" =>
      context.stop(child); lastSender = sender()
    case Terminated(`child`) => lastSender ! "finished"
  }

}
