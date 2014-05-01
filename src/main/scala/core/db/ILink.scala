package core.db

/**
 * Интерфейс для связей между объектами
 */
trait ILink {
  val sourceId: Int
  val targetId: Int
}
