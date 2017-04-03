package mitm

import scala.collection.mutable
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ HttpApp, RequestContext, Route, RouteResult }
import akka.stream.ActorMaterializer

private[mitm] trait Proxy {
  private[mitm] val route: Route
  private[mitm] val arguments: Arguments
  def run(): Unit = {
    implicit val system: ActorSystem = ActorSystem("mitm")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher
    println(arguments)
    Http().bindAndHandle(route, "localhost", arguments.port)
    println("run end")
  }
}

object Proxy {
  trait Builder extends AnyRef with akka.http.scaladsl.server.Directives with mitm.Directives {
    self =>
    private val config: String = ""
    private val routes: mutable.Seq[Route] = mutable.Seq.empty
    def intercept(rt: Route): Unit = self.routes.update(self.routes.size, rt)

    private[mitm] def build(_config: Arguments): Proxy = new Proxy { self =>
      override val arguments: Arguments = _config
      override val route: Route = concat(routes: _*)
    }
  }
}