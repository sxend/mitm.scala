package mitm

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{ Http, HttpExt }
import akka.stream.ActorMaterializer

import scala.collection.mutable

private[mitm] trait Proxy {
  private[mitm] val route: Route
  private[mitm] val arguments: Arguments
  protected implicit val system: ActorSystem
  protected implicit val materializer: ActorMaterializer
  protected val http: HttpExt
  def run(): Unit = {
    implicit val ec = system.dispatcher
    http.bindAndHandle(route, "localhost", arguments.port)
  }
}

object Proxy {
  trait Builder extends AnyRef with akka.http.scaladsl.server.Directives with mitm.Directives {
    builder =>
    protected implicit lazy val system: ActorSystem = ActorSystem("mitm")
    protected implicit lazy val materializer: ActorMaterializer = ActorMaterializer()(system)
    protected lazy val http: HttpExt = Http()(system)

    private lazy val routes: mutable.Seq[Route] = mutable.Seq.empty
    def intercept(rt: Route): Unit = routes.update(routes.size, rt)

    private[mitm] def build(_config: Arguments): Proxy = new Proxy { self =>
      override val arguments: Arguments = _config
      override val route: Route = concat(builder.routes: _*)
      override protected implicit val system: ActorSystem = builder.system
      override protected implicit val materializer: ActorMaterializer = builder.materializer
      override protected val http: HttpExt = builder.http
    }
  }
}