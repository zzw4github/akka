package me.zzw.app.fsm

import akka.actor.{ActorRef, ActorSystem, FSM}
import scala.concurrent.duration._

// received events
final case class SetTarget(ref: ActorRef)
final case class Queue(obj: Any)
case object Flush

// sent events
final case class Batch(obj: scala.collection.immutable.Seq[Any])

// states
sealed trait State
case object Idle extends State
case object Active extends State


sealed trait Data
case object Uninitialized extends Data
final case class Todo(target: ActorRef, queue:scala.collection.immutable.Seq[Any] ) extends Data
/**
  * Created by infosea on 2016-09-28.
  */
class Buncher extends FSM[State, Data]{
  startWith(Idle, Uninitialized)

  when(Idle) {
    case Event(SetTarget(ref), Uninitialized) =>
      stay using Todo(ref, Vector.empty)
  }

  // transition elided
  when(Active, stateTimeout = 1 second){
    case Event(Flush | StateTimeout, t: Todo) =>
      goto(Idle) using t.copy(queue = Vector.empty)
  }

  whenUnhandled {
    // common code for both states
    case Event(Queue(obj), t @ Todo(_, v)) =>
      goto(Active) using t.copy(queue = v :+ obj)

    case Event(e,s) =>
      log.warning("received unhandled request {} in state {} /{}", e, stateName, s)
      stay
  }

  onTransition { case Active -> Idle =>
    stateData match {
      case Todo(ref, queue) => ref ! Batch(queue)
      case _ => //
    }
  }

  // unhandled elided
  initialize()
}


