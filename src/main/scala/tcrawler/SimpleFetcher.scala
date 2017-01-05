package tcrawler

import org.apache.commons.httpclient.ConnectTimeoutException
import org.apache.commons.httpclient.Header

/**
 * Simple fetcher (Recommend for user)
 * @author mclaren
 */
class SimpleFetcher[T <: Fetchable] extends ConcurrentCrawler[T] {
  /**
   * Returns list of specified entity which was associated with the incoming URL has been fetched
   * @param url  incoming specified URL
   * @param parser: page content parser, It provide a transformer for "Original HTTP response" to list of the specified entity
   * @param hasBeenDisabled : Returns test if the current communication channel has been disabled by the target web-site
   * @param howToContinue: Provide a strategy that described how to continue the fetch task when the communication has been disabled by the target web-site
   */
  def fetch(url: String)(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): List[T] = {
    var retry: String => List[T] = fetchUrl => {
      logger.info("Network overload, crawler will take a rest at a moment")
      Thread.sleep(3000)
      this.fetch(fetchUrl)(parser)(hasBeenDisabled)(howToContinue)
    }

    try {
      var rawContent = InternetIO.fromUrl(url).getBytes
      //process disabled
      if (hasBeenDisabled(rawContent)) throw CommunicationChannelDisabledException(url)
      parser(rawContent)
    } catch {
      case e: CommunicationChannelDisabledException => howToContinue(url)
      case e: java.net.SocketException => retry(url)
      case e: java.net.SocketTimeoutException => retry(url)
      case e: ConnectTimeoutException => retry(url)
    }
  }

  def fetch_(url: String)(headers: Array[Header])(parser: Array[Byte] => List[T])(hasBeenDisabled: Array[Byte] => Boolean)(howToContinue: String => List[T]): List[T] = {
    var retry: String => List[T] = fetchUrl => {
      logger.info("Network overload, crawler will take a rest at a moment")
      Thread.sleep(3000)
      this.fetch_(fetchUrl)(headers)(parser)(hasBeenDisabled)(howToContinue)
    }

    try {
      var rawContent = InternetIO.fromUrl(url, headers).getBytes
      //process disabled
      if (hasBeenDisabled(rawContent)) throw CommunicationChannelDisabledException(url)
      parser(rawContent)
    } catch {
      case e: CommunicationChannelDisabledException => howToContinue(url)

      case e: java.net.SocketException => retry(url)
      case e: java.net.SocketTimeoutException => retry(url)
      case e: ConnectTimeoutException => retry(url)
    }
  }
}