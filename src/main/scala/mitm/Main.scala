package mitm

import java.io.File

import com.twitter.util.Eval
import mitm.Proxy.Builder

object Main {

  def main(args: Array[String]): Unit = {
    val config = parseConfig(args)
    val builder = config.file match {
      case Some(f) => new Eval(None).apply[Proxy.Builder](f)
      case _       => new Builder {}
    }
    builder.build(config).run()
  }
  private def parseConfig(args: Array[String]) = {
    parser.parse(args, Arguments()) match {
      case Some(config) => config
      case None         => throw new RuntimeException("failed to parse option.")
    }
  }
  private val parser = new scopt.OptionParser[Arguments]("mitm.scala") {
    help("help").text("Usage: mitm.scala [-p <port>] <config file name>")

    opt[File]('c', "config").action((x, c) =>
      c.copy(file = Option[File](x))).text("config file e.g. ./MitmProxy.scala")

    opt[Int]('p', "port").action((x, c) =>
      c.copy(port = x)).text("port is binding port")

  }

}

case class Arguments(port: Int = 8888, file: Option[File] = None)