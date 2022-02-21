package hc.system.config

final case class PostgresConfig(
                           className: String,
                           url: String,
                           user: String,
                           password: String
                         )
