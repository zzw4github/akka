package me.zzw.app.future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by infosea on 2016-09-29.
  */
object Future4 extends App{
  val f = for {
    a <- Future(10 / 2)
    b <- Future(a + 1)
    c <- Future(a - 1)
    if c > 3
  } yield b * c

  f.onSuccess({case result:Int => println (result)} )

  Thread sleep 1000
  f foreach println


}
