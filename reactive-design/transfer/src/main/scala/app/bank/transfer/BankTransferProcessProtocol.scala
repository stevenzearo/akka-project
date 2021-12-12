package app.bank.transfer

import akka.actor.ActorRef

object BankTransferProcessProtocol {

  sealed trait BankTransferProcessMessage

  final case class TransferFounds(
                                   transactionId: String,
                                   fromAccount: ActorRef,
                                   toAccount: ActorRef,
                                   amount: Double) extends BankTransferProcessMessage

  object BankTransferProcess {

    final case class FundsTransferred(transactionId: String)

    final case class FundsTransferFailed(transactionId: String)

    final case object Acknowledgment

  }

  object AccountProtocol {

    sealed trait AccountProtocolMessage

    final case class Withdraw(amount: Double) extends AccountProtocolMessage

    final case class Deposit(amount: Double) extends AccountProtocolMessage

  }

}
