package app.receiver.api.connection

case class ConnectionResponse(id: String, fromPath: String, toPath: String)

object ConnectionResponse {
  def apply(id: String, fromPath: String, toPath: String): ConnectionResponse = new ConnectionResponse(id, fromPath, toPath)

  def unapply(connectionResponse: ConnectionResponse): Option[(String, String, String)]
  = if (Option(connectionResponse).isEmpty) None else Some(connectionResponse.id, connectionResponse.fromPath, connectionResponse.toPath)
}