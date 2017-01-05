package tcrawler

/**
 * Fetchable object,Any fetched object should be inherited from this type
 * @author mclaren
 */
abstract class Fetchable {
  /**
   * Returns identifier of the current fetchable context
   */
  def getIdentifier(): String
}