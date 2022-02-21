package hc.system.config

final case class Configuration(
                                postgresConfig: PostgresConfig,
                                yamlConfig: YamlConfig
                              )