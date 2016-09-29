package me.zzw.app.future

//import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by infosea on 2016-09-29.
  */
object Future2 extends App{
  val future2 = Future {"Hell" + "World"}
  val f2 = future2 map {x => x.length}
  f2 foreach println

  Thread sleep( 100)
  if (f2.isCompleted)
    println (f2.value)
    f2.onComplete(println _)

}
