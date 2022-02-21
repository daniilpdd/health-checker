package hc

import hc.data.Endpoint
import zio.{Has, Task, ZIO}

package object endpoints {
  type Endpoints = Has[Endpoints.Service]

  object Endpoints {
    trait Service {
      def load: Task[Seq[Endpoint]]
    }

    def load: ZIO[Endpoints, Throwable, Seq[Endpoint]] = ZIO.accessM[Endpoints](_.get.load)
  }
}
