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

import com.twitter.finagle.http.Status
import io.finch._
import zio._
import zio.interop.catz._
import io.circe.generic.auto._
import io.finch.circe._
import io.codeeng.Configuration.http.externalUrl
import shapeless.{:+:, CNil}

class TodoEndpoints(todoRepository: TodoRepository)(implicit
    runtime: Runtime[ZEnv]
) extends EndpointModule[Task] {
  import TodoEndpoints._
  private val todosRoot = path("api") :: "todos"

  private def getOne: Endpoint[Task, Todo] =
    get(todosRoot :: path[Int]) { id: Int =>
      todoRepository.findOne(id).map {
        case Some(todo) => Ok(todo)
        case None       => Output.empty(Status.NotFound)
      }
    }

  private def getAll: Endpoint[Task, Seq[Todo]] =
    get(todosRoot) {
      todoRepository.findAll().map { todos =>
        Ok(todos)
      }
    }

  private def createTodo: Endpoint[Task, Unit] =
    post(todosRoot :: jsonBody[TodoRequest]) { tr: TodoRequest =>
      todoRepository.create(Todo(None, tr.title, tr.description)).map { id =>
        Output
          .unit(Status.Created)
          .withHeader("Location" -> s"$externalUrl/api/todos/$id")
      }
    }

  private def updateTodo: Endpoint[Task, Unit] =
    put(todosRoot :: path[Int] :: jsonBody[TodoRequest]) { (id: Int, tr: TodoRequest) =>
      todoRepository.update(Todo(Some(id), tr.title, tr.description)).map { _ =>
        Output
          .unit(Status.Ok)
      }
    }

  private def deleteTodo: Endpoint[Task, Unit] =
    delete(todosRoot :: path[Int]) { id: Int =>
      todoRepository.delete(id).map { _ =>
        Output
          .unit(Status.NoContent)
      }
    }

  def endpoints: Endpoint[Task, Todo :+: Seq[Todo] :+: Unit :+: Unit :+: Unit :+: CNil] =
    getOne :+: getAll :+: createTodo :+: updateTodo :+: deleteTodo
}

object TodoEndpoints {
  case class TodoRequest(title: String, description: String)

  def apply(
      todoRepository: TodoRepository
  )(implicit
      runtime: Runtime[ZEnv]
  ): Endpoint[Task, Todo :+: Seq[Todo] :+: Unit :+: Unit :+: Unit :+: CNil] =
    new TodoEndpoints(todoRepository).endpoints
}
