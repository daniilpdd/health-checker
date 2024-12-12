package org.daniilpdd.hc.data

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class Endpoint(
                   url: String,
                   period: Long = 1000
                   )

object Endpoint {
  implicit val encode: JsonEncoder[Endpoint] = DeriveJsonEncoder.gen[Endpoint]
  implicit val decode: JsonDecoder[Endpoint] = DeriveJsonDecoder.gen[Endpoint]
}