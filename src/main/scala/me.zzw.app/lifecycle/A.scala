package me.zzw.app.lifecycle

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorContext, SupervisorStrategy}

/**
  * Created by infosea on 2016-09-30.
  */
class A extends Actor{



  override protected[akka] def aroundReceive(receive: Receive, msg: Any): Unit = super.aroundReceive(receive, msg)

  override protected[akka] def aroundPreStart(): Unit = super.aroundPreStart()

  override protected[akka] def aroundPostStop(): Unit = super.aroundPostStop()

  override protected[akka] def aroundPreRestart(reason: Throwable, message: Option[Any]): Unit = super.aroundPreRestart(reason, message)

  override protected[akka] def aroundPostRestart(reason: Throwable): Unit = super.aroundPostRestart(reason)

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = super.preStart()

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = super.postStop()

  @scala.throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = super.preRestart(reason, message)

  @scala.throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = super.postRestart(reason)

  override def unhandled(message: Any): Unit = super.unhandled(message)

  def receive:Receive = {
    case "" =>
    case _ =>
  }
}
