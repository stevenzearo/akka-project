package app.receiver.api

import app.receiver.api.connection.ListConnectionResponse

trait MessageConnectionWebService {
  def listConnections(): ListConnectionResponse
}
