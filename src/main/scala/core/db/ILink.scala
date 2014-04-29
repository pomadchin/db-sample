package core.db

/**
 * Интерфейс для связей между объектами
 */
trait ILink {
  val source: Int
  val target: Int
}
