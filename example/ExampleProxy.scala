
new mitm.Proxy.Builder {
  intercept {
    case (req, res) =>
      println(req)
      res
  }
}
