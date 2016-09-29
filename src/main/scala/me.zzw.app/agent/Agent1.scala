package me.zzw.app.agent

import akka.agent.Agent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by infosea on 2016-09-29.
  */
object Agent1 extends App{
  val agent = Agent(4)
  agent send 6
  agent send (_ + 1)
  agent send (_ * 2)
//  agent alter 5
  Thread sleep 20
  println (agent())
  println (agent.get )

  val f1: Future[Int] = agent alter 7
   // alter a function
  val f2: Future[Int] = agent alter (_ + 1)
  val f3: Future[Int] = agent alter (_ * 2)
  // the ExecutionContext you want to run the function on
  // alterOff a function
  def f(a: Int ):Int = {
    Thread sleep 2000
    a + 5
  }
  val f4: Future[Int] = agent alterOff f


  Thread sleep 3000
  f4 foreach println
}
