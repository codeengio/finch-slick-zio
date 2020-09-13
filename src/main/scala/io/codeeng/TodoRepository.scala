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
import slick.jdbc.H2Profile.api._
import io.codeeng.DB._
import slick.lifted.ProvenShape
import zio.Task

object TodoRepository {
  class Todos(tag: Tag) extends Table[Todo](tag, "todos") {
    def id: Rep[Int]                   = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def title: Rep[String]             = column[String]("title")
    def description: Rep[String]       = column[String]("description")
    override def * : ProvenShape[Todo] = (id.?, title, description) <> (Todo.tupled, Todo.unapply)
  }
}

class TodoRepository {
  import TodoRepository._
  private val todos = TableQuery[Todos]

  def findAll(): Task[Seq[Todo]] = db.run(todos.sortBy(_.title).result)

  def findOne(id: Int): Task[Option[Todo]] = db.run(todos.filter(_.id === id).result.headOption)

  def create(todo: Todo): Task[Int] = db.run((todos returning todos.map(_.id)) += todo)

  def update(todo: Todo): Task[Int] = db.run(todos.filter(_.id === todo.id).update(todo))

  def delete(id: Int): Task[Int] = db.run(todos.filter(_.id === id).delete)
}
