package hc.data

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.util.Date

case class Check(
                  url: String,
                  timeout: Long,
                  time: Date
                )

object Check {
  implicit val encode: JsonEncoder[Endpoint] = DeriveJsonEncoder.gen[Endpoint]
  implicit val decode: JsonDecoder[Endpoint] = DeriveJsonDecoder.gen[Endpoint]
}