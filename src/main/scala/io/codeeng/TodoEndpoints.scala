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

import com.twitter.finagle.Service
import com.twitter.finagle.http.{ Request, Response, Status }
import io.finch._
import zio.Task
import zio._
import zio.interop.catz._
import io.finch.circe._
import io.circe.generic.auto._
import shapeless.{ :+:, CNil }

class TodoEndpoints[R](todoRepository: TodoRepository)(implicit runtime: Runtime[R])
    extends EndpointModule[Task] {
  private def getTodo: Endpoint[Task, Todo] =
    get("api" :: path[Int]) { id: Int =>
      todoRepository.findOne(id).map {
        case Some(todo) => Ok(todo)
        case None       => Output.empty(Status.NotFound)
      }
    }

  private def getAllTodos: Endpoint[Task, Seq[Todo]] =
    get("api") {
      todoRepository.findAll().map { todos =>
        Ok(todos)
      }
    }

  def endpoints: Endpoint[Task, Todo :+: Seq[Todo] :+: CNil] = getTodo :+: getAllTodos
}

object TodoEndpoints {
  def apply[R](
      todoRepository: TodoRepository
  )(implicit runtime: Runtime[R]): Service[Request, Response] =
    new TodoEndpoints(todoRepository).endpoints.toService
}
