package me.zzw.app.http

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler, Route, RoutingLog}
import akka.stream.{ActorMaterializer, Materializer}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.settings.{ParserSettings, RoutingSettings}
import akka.stream.scaladsl.Flow
import spray.json.DefaultJsonProtocol._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.collection.mutable.Map


/**
  * Created by infosea on 2016-09-27.
  */
object WebServer2 {
  // domain model
  final case class Item(name: String, id: Long)
  final case class Order(items: List[Item])

  // formats for unmarshalling and marshalling
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order)

  val items:Map[Long,Item] = Map(1L -> Item("item1",1L), 2L->Item("item2",2L))
  // (fake) async database query api
//  import scala.concurrent.ExecutionContext.Implicits.global
  def fetchItem(itemId: Long)(implicit e :ExecutionContext): Future[Option[Item]] = Future{items.get(itemId)}
  def saveOrder(order: Order)(implicit e :ExecutionContext): Future[Done] = Future{
    order.items foreach(
      item => items += (3L -> item)
      )
    Done
  }

  def main(args: Array[String]): Unit = {

    // needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // needed for the future map/flatmap in the end

    implicit val executionContext = system.dispatcher

    val route =
      get {
        pathPrefix("item" / LongNumber) {id =>
        // there might be no item for a given id
          val maybeItem: Future[Option[Item]] = fetchItem(id)
          onSuccess(maybeItem) {
            case Some(item) => complete(item)
            case None => complete(StatusCodes.NotFound)
          }
        }
      } ~
      post {
        path("create-order") {
          entity(as[Order]) {order =>
            val saved: Future[Done] = saveOrder(order)
            onComplete(saved) {done => complete("order created")
            }
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080\nPress RETURN to stop...")
    StdIn.readLine()
    // let it run until user press return
    bindingFuture.flatMap(_.unbind())
    // trigger unbinding from the post
      .onComplete(_ => system.terminate())
    // and shutdown when done
  }
}

