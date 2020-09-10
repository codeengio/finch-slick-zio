/*
 * Copyright (c) 2020 Branislav Lazic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.codeeng
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finagle.{ Http, ListeningServer, Service }
import com.twitter.util.Future
import io.finch.ToAsync
import zio._
import zio.interop.catz._

object Main extends CatsApp {

  private def closeLater(listeningServer: ListeningServer): ZIO[Any, Nothing, Any] =
    Task
      .effectSuspend(implicitly[ToAsync[Future, Task]].apply(listeningServer.close()))
      .catchAll(t => UIO("Failed to close the server", t))

  private def serve(
      address: String,
      service: Service[Request, Response]
  ): ZManaged[Any, Throwable, ListeningServer] =
    Managed.makeEffect(Http.server.serve(address, service))(closeLater)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    for {
      exitCode <- serve(":8000", TodoEndpoints(new TodoRepository)(implicitly[Runtime[Any]]))
        .use(_ => ZIO.never)
        .catchAll(t => UIO("Failed to start the server", t))
        .map(_ => zio.ExitCode.success)

    } yield exitCode
}
