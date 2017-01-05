package tcrawler

/**
 * If communication channel has been disabled the throw the type of <code>CommunicationChannelDisabledException</code>
 */
case class CommunicationChannelDisabledException(message: String) extends Exception(message)

/**
 * If communication is not matched by HTTP protocol then throw a type of <code>BadRequestException</code>
 */
case class BadRequestException(message: String) extends Exception(message)