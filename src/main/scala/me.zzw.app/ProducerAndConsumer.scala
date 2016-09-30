package me.zzw.app

import akka.actor.Actor

/**
  * Created by infosea on 2016-09-30.
  */
class Producer(name: String){}
class Consumer(name: String){}

class ProducerAndConsumer(list: List[String]) extends Actor{
  def receive ={
    case "put" =>
      list.+:( new Producer("1"))
    case "get" =>
      list.drop(0)
  }

}
