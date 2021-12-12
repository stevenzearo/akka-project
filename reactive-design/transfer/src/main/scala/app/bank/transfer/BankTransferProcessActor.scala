package app.bank.transfer

import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout}
import app.bank.transfer.BankTransferProcessProtocol.AccountProtocol.{Deposit, Withdraw}
import app.bank.transfer.BankTransferProcessProtocol.BankTransferProcess.{Acknowledgment, FundsTransferFailed, FundsTransferred}
import app.bank.transfer.BankTransferProcessProtocol._

import scala.concurrent.duration.Duration

class BankTransferProcessActor(receiveTimeoutDuration: Duration) extends Actor {
  context.setReceiveTimeout(receiveTimeoutDuration)

  override def receive: Receive = {
    case TransferFounds(transactionId, fromAccount, toAccount, amount) =>
      fromAccount ! Withdraw(amount)
      val client: ActorRef = sender()
      context.become(awaitWithdraw(transactionId, toAccount, amount, client))
  }

  def awaitWithdraw(transactionId: String, toAccount: ActorRef, amount: Double, client: ActorRef): Receive = {
    case Acknowledgment =>
      toAccount ! Deposit(amount)
      context.become(awaitDeposit(transactionId, client))
    case ReceiveTimeout =>
      client ! FundsTransferFailed(transactionId)
      context.stop(self)
  }

  def awaitDeposit(transactionId: String, client: ActorRef): Receive = {
    case Acknowledgment =>
      client ! FundsTransferred(transactionId)
      context.stop(self)
    case ReceiveTimeout =>
      client ! FundsTransferFailed(transactionId)
      context.stop(self)
  }
}

object BankTransferProcessActor {
  def props(receiveTimeout: Duration): Props = Props(new BankTransferProcessActor(receiveTimeout))
}
