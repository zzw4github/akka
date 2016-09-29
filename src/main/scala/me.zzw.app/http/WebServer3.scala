package me.zzw.app.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import scala.util.Random

/**
  * Created by infosea on 2016-09-28.
  */
object WebServer3 {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // needed for the future flatmap/onComplete in the end
    implicit val executionContext = system.dispatcher

    // streams are re-usable so we can define it here
    // and use it for every request
    val numbers = Source.fromIterator(() => Iterator.continually(Random.nextInt()))

    val route =
      path("random") {
        get {
          complete(
            HttpEntity(ContentTypes.`text/plain(UTF-8)`,
            // transform each number to a chunk of bytes
              numbers.map(n => ByteString(s"$n\n"))
            )
          )
        }
      }

    val bindFuture = Http().bindAndHandle(
      route, "localhost", 8082
    )
    println(s"server online at http://localhost:8082/\n Press RETURN to stop...")
    StdIn.readLine()
      // let it run until user presss return
      bindFuture.flatMap(_.unbind())
      // trigger unbinding from the port
        .onComplete(_ => system.terminate)
      // and shutdown when done
      }
}
