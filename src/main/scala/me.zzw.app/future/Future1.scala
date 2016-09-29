package me.zzw.app.future

/**
  * Created by infosea on 2016-09-28.
  */



import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future, Promise}
object Future1 extends App{
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())

  def doSomething(arg:String): String =  {arg}

  val f= Future {
    doSomething("some")
  }

   val p = Promise.successful("foo")

  ec.shutdown()
}
