package mitm

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.server.{ Route, RouteResult }
import akka.stream.ActorMaterializer

trait Directives {
  protected implicit val system: ActorSystem
  protected implicit val materializer: ActorMaterializer
  protected val http: HttpExt
  def remote(scheme: String = "http", host: String = "localhost", port: Int = 80): Route = { ctx =>
    import ctx.executionContext
    val request = ctx.request.copy(uri = toUri(scheme, host, port))
    for {
      response <- http.singleRequest(request)(materializer)
    } yield RouteResult.Complete(response)
  }
  private def toUri(scheme: String, host: String, port: Int): String = {
    val uriPart = (scheme, port) match {
      case ("http", 80) | ("https", 443) => ""
      case _                             => s":$port"
    }
    s"$scheme://$host$uriPart"
  }
}
