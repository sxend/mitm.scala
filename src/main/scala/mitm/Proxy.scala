package mitm

import scala.concurrent.Future
import scala.collection.mutable
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.{ HttpApp, RequestContext, RouteResult }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

private[mitm] trait Proxy {
  import Proxy._

  val interceptors: Set[Interceptor]
  def run(): Unit = {
    new HttpApp {
      implicit private val s: ActorSystem = ActorSystem("mitm")
      implicit private val m = ActorMaterializer()
      implicit private val ec = s.dispatcher
      override protected def route = { ctx: RequestContext =>
        Http().singleRequest(ctx.request).map { res =>
          println(s"proxy logging: $res")
          RouteResult.Complete(res)
        }
      }
    }.startServer("localhost", 8888)
  }
}

object Proxy {
  type Interceptor = PartialFunction[(String, Future[String]), Future[String]]
  trait Builder {
    self =>
    private val interceptors: mutable.Set[Interceptor] = mutable.Set.empty
    def intercept(fn: Interceptor): Unit = {
      interceptors.update(fn, included = true)
    }
    private[mitm] def build: Proxy = {
      new Proxy {
        override val interceptors: Set[Interceptor] = self.interceptors.toSet
      }
    }
  }
}