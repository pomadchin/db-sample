package core.db

/**
 * Интерфейс для связей между объектами
 */
trait ILink extends Serializable {
  val sourceId: Int
  val targetId: Int
}
