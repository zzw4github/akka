package me.zzw.app.future

import scala.concurrent.{ Future}
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by infosea on 2016-09-29.
  */
object Future3 extends App{
  val future1 = Future.successful(4)
  val future2 = future1.filter(_ % 2 == 0)
  future2 foreach( println)

  val failedFilter = future1.filter(_ % 2 == 1).recover({
    case m: NoSuchElementException => 0
  })

  failedFilter foreach( println)
}
