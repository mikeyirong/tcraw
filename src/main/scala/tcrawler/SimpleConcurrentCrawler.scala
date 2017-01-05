package tcrawler

trait SimpleConcurrentCrawler[T <: Fetchable] extends ConcurrentCrawler[T] {
  override def concurrent_fetch(url: String)(paginition: String => Array[String])(processor: List[T] => Unit)(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): Unit = {
    null.asInstanceOf[List[T]]
  }
}