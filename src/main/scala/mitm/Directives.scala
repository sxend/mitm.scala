package mitm

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Future
import scala.util.{Failure, Success}
trait Directives {
  protected val system: ActorSystem
  private val http = Http()(system)
  def remote(url: String): Route = {
    onComplete(http.singleRequest(HttpRequest(uri = url))) {
      case Success(s) => (_) => Future.successful(RouteResult.Complete(s))
      case Failure(t) => failWith(t)
    }
  }
}
