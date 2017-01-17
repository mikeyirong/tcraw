package tcrawler

import java.util.concurrent.ForkJoinPool
import JavaVirtualMachine._
import org.w3c.dom.Document
import java.util.concurrent.RecursiveTask
import org.apache.commons.httpclient.NameValuePair

/**
 * Concurrent crawler
 * @author mclaren
 */
trait ConcurrentCrawler[T <: Fetchable] extends TCrawler[T] {
  /**
   * High performance concurrent fork/join pool
   * @see java.util.concurrent.ForkJoinPool
   */
  val fkPool = new ForkJoinPool("tcrawler.concurrent".<::("100").toInt)
  /**
   * Returns list of specified entity that the incoming URL has been fetched
   * @param url  incoming specified URL
   * @param parser: page content parser, It provide a transformer for "Original HTTP response" to list of the specified entity
   * @param hasBeenDisabled : Returns test if the current communication channel has been disabled by the target web-site
   * @param howToContinue: Provide a strategy the described how to continue the fetch task when the communication has been disabled by the target web-site
   */
  def concurrent_fetch(url: String)(paginition: String => Array[String])(processor: List[T] => Unit)(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): Unit = {
    this.fkPool.submit(new RecursiveTask[Unit] {
      /* fork */
      def compute(): Unit = paginition(url).map(pageUrl => {
        var task = (new RecursiveTask[Unit] {
          def compute(): Unit = processor(fetch(pageUrl)(parser)(hasBeenDisabled)(howToContinue))
        })
        task.fork
        task
      }).foreach(_.join)
    }).get /* waiting for completion */
  }

  /**
   * Returns list of specified entity that the incoming URL has been fetched
   * @param urls  incoming specified URL list
   * @param parser: page content parser, It provide a transformer for "Original HTTP response" to list of the specified entity
   * @param hasBeenDisabled : Returns test if the current communication channel has been disabled by the target web-site
   * @param howToContinue: Provide a strategy the described how to continue the fetch task when the communication has been disabled by the target web-site
   */
  def concurrent_fetch(urls: Array[String])(processor: List[T] => Unit)(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): Unit = {
    this.fkPool.submit(new RecursiveTask[Unit] {
      def compute(): Unit = urls.map(url => {
        var task = (new RecursiveTask[Unit] {
          def compute(): Unit = processor(fetch(url)(parser)(hasBeenDisabled)(howToContinue))
        })
        task.fork()
        task
      }).foreach(_.join())
    }).get
  }

  /**
   * Returns list of specified entity that the incoming URL has been fetched
   * @param urls  incoming specified URL list
   * @param parser: page content parser, It provide a transformer for "Original HTTP response" to list of the specified entity
   * @param hasBeenDisabled : Returns test if the current communication channel has been disabled by the target web-site
   * @param howToContinue: Provide a strategy the described how to continue the fetch task when the communication has been disabled by the target web-site
   */
  def concurrent_fetch_post(urls: Array[String])(body: Array[NameValuePair])(processor: List[T] => Unit)(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): Unit = {
    this.fkPool.submit(new RecursiveTask[Unit] {
      def compute(): Unit = urls.map(url => {
        var task = (new RecursiveTask[Unit] {
          def compute(): Unit = processor(fetch_post(url)(parser)(hasBeenDisabled)(howToContinue)(body))
        })
        task.fork()
        task
      }).foreach(_.join())
    }).get
  }
}