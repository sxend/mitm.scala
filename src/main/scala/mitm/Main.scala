package mitm

import com.twitter.util.Eval

object Main {
  def main(args: Array[String]): Unit = {
    // TODO: option parse
    println(args(0))
    val builder = new Eval(None).apply[Proxy.Builder](new java.io.File(args(0)))
    builder.build.run()
  }
}
