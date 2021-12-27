package core.db

import scala.language.postfixOps

/**
 * Base table trait
 *
 * T - Table object type
 */
trait IEntityTable[T <: Identifiable]:
  val fileName = "tmp/" + this.getClass.getName.split("\\$").last

  var list: List[T] = List[T]()

  /**
   * Add object in the table
   *
   * @param obj
   *   Object to be inserted
   * @return
   *   Object id
   */
  def Add(obj: T): Int =
    if list.nonEmpty then obj.id = Option(list.last.id.getOrElse(0) + 1)
    else obj.id = Option(0)
    list = list :+ obj
    obj.id.getOrElse(0)

  /**
   * Get object by its id
   *
   * @param id
   *   Object id
   * @return
   *   Object of type T
   */
  def Get(id: Int): T = list.filter(_.id.getOrElse(0) == id) head

  /**
   * Delete object by its id
   *
   * @param id
   *   Id of object to be deleted
   */
  def Delete(id: Int): Unit = list = list.filter(_.id.getOrElse(0) != id)

  def DeleteCascade(id: Int): Unit
