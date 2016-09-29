package me.zzw.app.actors.api

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify, Terminated}

/**
  * Created by infosea on 2016-09-27.
  */
class Follower extends Actor{
  val identifyId = 1
  context.actorSelection("/user/another") ! Identify(identifyId)


  def receive = {
    case ActorIdentity(`identifyId`, Some(ref)) =>
      context.watch(ref)
      context.become(active(ref))
    case ActorIdentity(`identifyId`,None) =>
      context.stop(self)
  }

  def active(another: ActorRef): Actor.Receive = {
    case Terminated(`another`) => context.stop(self)
  }

}
