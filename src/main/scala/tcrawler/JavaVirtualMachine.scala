package tcrawler

/**
 * This object is mapped to the current underlining JVM
 */
object JavaVirtualMachine {
  /**
   * Returns property value from the JVM context which was mapped by the incoming key
   */
  def jvm_property(configKey: String) = System.getProperty(configKey)

  /**
   * Returns environment value from the current OS process which was mapped by the incoming key
   */
  def jvm_env(configKey: String) = System.getenv(configKey)

  /**
   * implicit definitions
   */
  implicit class StringLike(literal: String) {
    def jvm_property = JavaVirtualMachine.jvm_property(literal)
    def jvm_env = JavaVirtualMachine.jvm_env(literal)
    def config: String = jvm_property match {
      case value: String => value
      case null => jvm_env
    }

    def <::(default: String): String = config match {
      case null => default
      case value: String => value
    }
  }
}