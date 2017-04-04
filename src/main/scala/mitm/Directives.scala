package mitm

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.{ RequestContext, Route, RouteResult }
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{ Failure, Success }
trait Directives {
  protected val system: ActorSystem
  protected val materializer: ActorMaterializer
  private val http = Http()(system)
  def remote(url: String): Route = { ctx =>
    import ctx.executionContext
    for {
      response <- http.singleRequest(ctx.request.copy(uri = url))(materializer)
    } yield RouteResult.Complete(response)
  }
}
