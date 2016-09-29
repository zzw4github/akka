package me.zzw.app.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

/**
  * Created by infosea on 2016-09-27.
  */
object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()

    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
        "<h1>Say hello to akka-http</h1>"))
      }
    }
    val bindingFuture = Http().bindAndHandle(route, "localhost",8081)
    println(s"Server online at http://localhost:8081/\nPress Return to stop...")
    StdIn.readLine()
    // let it run until user presses return
    bindingFuture.flatMap(_.unbind())
    // trigger unbinding from the port
      .onComplete(_ => system.terminate())
    // and shutdown when done
  }

}