package org.daniilpdd.healthchecker.model

case class HCConfig(
                   prometheusConfig: PrometheusConfig,
                   endpointsPath: String
                 )

case class PrometheusConfig(
                             host: String,
                             port: Int,
                             jobName: String
                           )
