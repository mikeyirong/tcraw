package tcrawler

import org.slf4j.LoggerFactory

/**
 * Provide logging behavior
 * @author mclaren
 */
trait LoggingSupport {
  /**
   * Logging facade
   */
  var logger = LoggerFactory.getLogger(getClass())
}