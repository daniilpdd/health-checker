package hc.data

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.util.Date

sealed trait CheckResult
case object Timeout extends CheckResult
case class StatusCode(code: Int) extends CheckResult

case class Check(
                  url: String,
                  timeout: Long,
                  time: Date,
                  status: CheckResult
                )

object Check {
  implicit val encode: JsonEncoder[Endpoint] = DeriveJsonEncoder.gen[Endpoint]
  implicit val decode: JsonDecoder[Endpoint] = DeriveJsonDecoder.gen[Endpoint]
}