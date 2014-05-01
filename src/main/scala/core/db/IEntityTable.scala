package core.db

import scala.language.postfixOps

/**
 * Интерфейс основной таблицы,
 * хранящей список объектов
 *
 * T - Тип объектов, хранимых в таблице
 */
trait IEntityTable[T] { outer =>
  val fileName = "tmp/" + outer.getClass.getName.split("\\$").last

  var list: List[T] = List[T]()

  /**
   * Добавление объекта в таблицу
   *
   * @param obj Добавляемый объект
   * @return Идентификатор, присвоенный объекту
   */
  def Add(obj: T): Int = {
    list = list :+ obj
    list.length - 1
  }

  /**
   * Получение объекта по его
   * идентификатору
   *
   * @param id Идентификатор объекта
   * @return Полученный объект либо null, если указанный объект не найден
   */
  def Get(id: Int): T = list(id)

  /**
   * Удаление объекта по его
   * идентификатору
   *
   * @param id Идентификатор удаляемого объекта
   */
  def Delete(id: Int) = list = list patch(id, Nil, 1)

}
