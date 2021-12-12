package app.bank.transfer

import akka.actor.{ActorRef, ActorSystem, ReceiveTimeout}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import app.bank.transfer.BankTransferProcessProtocol.AccountProtocol.{Deposit, Withdraw}
import app.bank.transfer.BankTransferProcessProtocol.BankTransferProcess.{Acknowledgment, FundsTransferFailed, FundsTransferred}
import app.bank.transfer.BankTransferProcessProtocol.TransferFounds
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.{AfterAll, BeforeEach, Test, TestInstance}
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.DurationInt

@Test
@TestInstance(Lifecycle.PER_CLASS)
class BankTransferProcessActorTest() extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with Matchers {
  final var bankTransferActorRef: ActorRef = _
  final var from: TestProbe = _
  final var to: TestProbe = _
  final var transactionId: String = _
  final var amount: Double = _

  @BeforeEach
  def beforeEach(): Unit = {
    bankTransferActorRef = system.actorOf(BankTransferProcessActor.props(10.minutes))
    from = TestProbe()
    to = TestProbe()
    transactionId = "transaction-0001"
    amount = 20.23
  }

  @AfterAll
  def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  @Test
  def transferSuccessTest(): Unit = {
    bankTransferActorRef ! TransferFounds(transactionId = transactionId, from.testActor, to.testActor, amount)
    from.expectMsg(5.seconds, Withdraw(amount))
    from.reply(Acknowledgment)
    to.expectMsg(5.seconds, Deposit(amount))
    to.reply(Acknowledgment)
    expectMsg(5.seconds, FundsTransferred(transactionId))
  }

  @Test
  def transferFailedWhenAwaitWithdrawTest(): Unit = {
    bankTransferActorRef ! TransferFounds(transactionId = transactionId, from.testActor, to.testActor, amount)
    bankTransferActorRef ! ReceiveTimeout.getInstance
    from.expectMsg(5.seconds, Withdraw(amount))
    to.expectNoMessage()
    expectMsg(5.seconds, FundsTransferFailed(transactionId))
  }

  @Test
  def transferFailedWhenAwaitDepositTest(): Unit = {
    bankTransferActorRef ! TransferFounds(transactionId = transactionId, from.testActor, to.testActor, amount)
    from.expectMsg(5.seconds, Withdraw(amount))
    from.reply(Acknowledgment)
    bankTransferActorRef ! ReceiveTimeout.getInstance
    to.expectMsg(5.seconds, Deposit(amount))
    expectMsg(5.seconds, FundsTransferFailed(transactionId))
  }
}
