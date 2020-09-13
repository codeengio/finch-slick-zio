package io

import zio.{Task, ZIO}

import scala.concurrent.Future

package object codeeng {
  implicit def asZIO[A](future: Future[A]): Task[A] = ZIO.fromFuture(implicit ec => future)
}
