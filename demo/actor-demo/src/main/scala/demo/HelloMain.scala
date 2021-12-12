package demo

import akka.actor.{ActorRef, ActorSystem, Props}

object HelloMain extends App {
  private val system: ActorSystem = ActorSystem("hello-world")
  private val greet: ActorRef = system.actorOf(Props[GreetActor]())
  private val greeted: ActorRef = system.actorOf(Props[GreetedActor]())
  greeted ! Message("hello", greet)
}
