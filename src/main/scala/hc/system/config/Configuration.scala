package hc.system.config

final case class Configuration(
                                postgresConfig: PostgresConfig,
                                jsonConfig: JsonConfig
                              )