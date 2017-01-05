package tcrawler

trait StringAdapter {
  def isEmpty(source: String): Boolean = return source == null || source.trim().length() == 0

  def isNotEmpty(source: String): Boolean = !isEmpty(source)

  def equal(one: String, other: String): Boolean = {
    if (one == null || other == null) false
    one.trim().equals(other.trim())
  }
}