package mitm

import scala.concurrent.Future

private[mitm] trait Proxy {
  def run(): Unit = {

  }
}

object Proxy {
  trait Builder {
    def intercept(fn: PartialFunction[(String, Future[String]), Future[String]]): Unit = {
      println("intercept")
    }
    private[mitm] def build: Proxy = {
      new Proxy {}
    }
  }
}