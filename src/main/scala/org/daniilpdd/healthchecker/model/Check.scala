package org.daniilpdd.healthchecker.model

import java.time.LocalDateTime

sealed trait CheckResult
case object Timeout extends CheckResult
case class StatusCode(code: Int) extends CheckResult
case class Error(errorMessage: String) extends CheckResult

case class Check(
                  url: String,
                  timeout: Long,
                  time: LocalDateTime,
                  status: CheckResult
                )
