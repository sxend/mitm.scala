new mitm.Proxy.Builder {
  logger.info("init example config")
  intercept {
    get(remote(host = "arimit.su"))
  }
}
