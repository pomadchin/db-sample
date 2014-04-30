package core.db

/**
 * Таблица связей между объектами
 *
 * T -- Тип связей, хранящихся в таблице
 */
trait ILinkTable[T <: ILink] extends Serializable {

  var list = List[T]()

  /**
   * Добавление связи
   *
   * @param link Добавляемая связь
   */
  def AddLink(link: T) =
    list = list :+ link

  /**
   * Добавление связи
   *
   * @param sourceId Идентификатор первого объекта (источника связи)
   * @param targetId Идентификатор второго объекта
   */
  def AddLink(sourceId: Int, targetId: Int)

  /**
   * Удаление связи
   *
   * @param sourceId Идентификатор первого объекта (источника связи)
   * @param targetId Идентификатор второго объекта
   */
  def DeleteLink(sourceId: Int, targetId: Int) =
    list = list filter (e => (e.sourceId != sourceId) && (e.targetId != targetId))

  /**
   * Получение списка идентификаторов объектов, связанных с указанным источником
   *
   * @param sourceId Идентификатор источника
   * @return Список идентификаторов
   */
  def GetTargetIds(sourceId: Int): List[Int] =
    list filter (_.sourceId == sourceId) map (_.targetId)

  /**
   * Получение идентификатора источника
   * по идентификатору одного из
   * связанных с ним объектов
   *
   * @param targetId Идентификатор связанного объекта
   * @return Идентификатор источника
   */
  def GetSourceId(targetId: Int): Int =
    list filter (_.targetId == targetId) map (_.sourceId) head

  /**
   * Получение объекта-связи
   *
   * @param sourceId Идентификатор первого объекта (источника связи)
   * @param targetId Идентификатор второго объекта
   * @return Объект связи
   */
  def GetLink(sourceId: Int, targetId: Int): T =
    list filter (e => (e.sourceId != sourceId) && (e.targetId != targetId)) head
}
