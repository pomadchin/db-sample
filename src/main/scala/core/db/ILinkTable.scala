package core.db
import scala.language.postfixOps

/**
 * Object relations trait
 *
 * T -- Relations type
 */
trait ILinkTable[T <: ILink] { outer ⇒
  val fileName = "tmp/" + outer.getClass.getName.split("\\$").last

  var list = List[T]()

  /**
   * Add a relation
   *
   * @param link Relation
   */
  def AddLink(link: T) = {
    DeleteLink(link.sourceId, link.targetId)

    list = list :+ link
  }

  /**
   * Add a relation
   *
   * @param sourceId Id of the first relation (source)
   * @param targetId Id of the second relation
   */
  def AddLink(sourceId: Int, targetId: Int)

  /**
   * Delete a relation
   *
   * @param sourceId Id of the first relation (source)
   * @param targetId Id of the second relation
   */
  def DeleteLink(sourceId: Int, targetId: Int) =
    list = list filter (e ⇒ e.sourceId != sourceId || e.targetId != targetId)

  /**
   * Get id list in relations with the source
   *
   * @param sourceId Id of the source
   * @return List of ids
   */
  def GetTargetIds(sourceId: Int): List[Int] =
    list filter (_.sourceId == sourceId) map (_.targetId)

  /**
   * Get ids of the source by one of its relational objects ids
   *
   * @param targetId Id of the target
   * @return Id of the source
   */
  def GetSourceId(targetId: Int): Int =
    list filter (_.targetId == targetId) map (_.sourceId) head

  /**
   * Get the relation
   *
   * @param sourceId Id of the first relation (source)
   * @param targetId Id of the second relation
   * @return Relation object
   */
  def GetLink(sourceId: Int, targetId: Int): T =
    list filter (e ⇒ (e.sourceId == sourceId) && (e.targetId == targetId)) head
}
