package me.zzw.app.fsm

/**
  * Created by infosea on 2016-09-28.
  */
import akka.actor.{ActorSystem, FSM, Props}

import scala.collection.immutable
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.testkit.{ImplicitSender, TestKit}
object FSMDocSpec //{
  // messages and data types
//}

class FSMDocSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike
  with Matchers with BeforeAndAfterAll {
  // fsm code elided ...
  "simple finite state machine" must {
    "demonstrate NullFunction" in {
      class A extends FSM[Int, Null] {
        val SomeState = 0
        when(SomeState)(FSM.NullFunction)
      }
    }


    "batch correctly" in {
      val buncher = system.actorOf(Props(classOf[Buncher], this))
      buncher ! SetTarget(testActor)
      buncher ! Queue(43)
      buncher ! Queue(42)
      expectMsg(Batch(immutable.Seq(43, 42)))
      buncher ! Queue(44)
      buncher ! Flush
      buncher ! Queue(45)
      expectMsg(Batch(immutable.Seq(44)))
      expectMsg(Batch(immutable.Seq(45)))
    }

    "not batch if uninitialized" in {
      val buncher = system.actorOf(Props(classOf[Buncher], this))
      buncher ! Queue(42)
      expectNoMsg
    }
  }

}
