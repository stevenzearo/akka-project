package app.bank.transfer

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.junit.jupiter.api.Test
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration.DurationInt

class DemoTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  @Test
  def registerTest(): Unit = {
    Logger(this.getClass.getCanonicalName).warn("testing...")
  }

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def testTransfer(): Unit = {
    val testProbe = TestProbe()
    val msg = "hello, world!"
    testProbe.ref ! msg
    expectMsg(5.seconds, msg)
  }
}