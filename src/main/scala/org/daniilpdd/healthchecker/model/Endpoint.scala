package org.daniilpdd.healthchecker.model

case class Endpoint(
                   url: String,
                   port: Int,
                   path: String,
                   period: Long
                   )
