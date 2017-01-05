package tcrawler

/**
 * Generic Crawler definition
 */
trait TCrawler[T <: Fetchable] extends LoggingSupport {
  /**
   * Returns list of specified entity that the incoming URL has been fetched
   * @param url  incoming specified URL
   * @param parser: page content parser, It provide a transformer for "Original HTTP response" to list of the specified entity
   * @param hasBeenDisabled : Returns test if the current communication channel has been disabled by the target web-site
   * @param howToContinue: Provide a strategy the described how to continue the fetch task when the communication has been disabled by the target web-site
   */
  def fetch(url: String)(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): List[T]
}