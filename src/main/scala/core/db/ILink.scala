package core.db

/**
 * Table relations trait
 */
trait ILink:
  val sourceId: Int
  val targetId: Int
