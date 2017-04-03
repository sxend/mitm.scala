new mitm.Proxy.Builder {
  intercept {
    (get & path("/"))(remote("http://akka.io/"))
  }
}
