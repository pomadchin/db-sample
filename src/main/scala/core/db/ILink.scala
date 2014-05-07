package core.db

/**
 * Интерфейс для связей между объектами
 */
trait ILink extends Identifiable {
  val sourceId: Int
  val targetId: Int
}
