package org.daniilpdd.hc.system.config

final case class Configuration(
                              prometheusConfig: PrometheusConfig,
                              jsonConfig: JsonConfig
                              )