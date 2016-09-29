package me.zzw.app.actors

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by infosea on 2016-09-27.
  */
class MyActor extends Actor{
  val log = Logging(context.system,this)
  def receive:Receive = {
    case "test" => log.info("received test")
    case _ => log.info("received unknown message")
  }
}
