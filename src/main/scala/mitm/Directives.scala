package mitm

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.server.{ Route, RouteResult }
import akka.stream.ActorMaterializer

trait Directives {
  protected implicit val system: ActorSystem
  protected implicit val materializer: ActorMaterializer
  protected val http: HttpExt
  def remote(url: String): Route = { ctx =>
    import ctx.executionContext
    for {
      response <- http.singleRequest(ctx.request.copy(uri = url))(materializer)
    } yield RouteResult.Complete(response)
  }
}
