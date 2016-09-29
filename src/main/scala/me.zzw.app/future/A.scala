package me.zzw.app.future

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.Future

/**
  * Created by infosea on 2016-09-29.
  */
class A extends Actor{
  import context.dispatcher
  val f = Future("Hello")
  def receive = {
    case "" => println("hello")
    case _ => println("-----")
  }

}

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
object A extends App{
  implicit val timeout = Timeout(5 seconds)
  val system = ActorSystem("hello")
  val actor = system.actorOf(Props(classOf[A]), "a")
  val future = actor ? "a"
  //enabled by the "ask" import
  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)
}
