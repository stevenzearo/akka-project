package app.receiver.api.connection

case class ListConnectionResponse(connections: List[ConnectionResponse])

object ListConnectionResponse {
  def apply(connections: List[ConnectionResponse]): ListConnectionResponse = new ListConnectionResponse(connections)

  def unapply(response: ListConnectionResponse): Option[List[ConnectionResponse]] = if (Option(response).isEmpty) None else Some(response.connections)
}
