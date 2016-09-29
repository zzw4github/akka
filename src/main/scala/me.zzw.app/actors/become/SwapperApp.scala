package me.zzw.app.actors.become

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

/**
  * Created by infosea on 2016-09-27.
  */
case object Swap
class Swapper extends Actor {
  import context._
  val log = Logging(system,this)

  def receive = {
    case Swap =>
      log.info("Hi")
      become({
        case Swap =>
          log.info("Ho")
          unbecome()
          // resets the latest 'become' (just for run)
      }, discardOld = false)
      // push on top instead of replace
  }
}
object SwapperApp extends App{
  val system = ActorSystem("SwapperSystem")
  val swap = system.actorOf(Props[Swapper],name = "swapper")
  swap ! Swap // logs Hi
  swap ! Swap // logs Ho
  swap ! Swap // logs Hi
  swap ! Swap // logs Ho
  swap ! Swap // logs Hi
  swap ! Swap // logs Ho


}
