package tcrawler

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptContext
import org.w3c.dom.Document
import org.cyberneko.html.parsers.DOMParser
import org.xml.sax.InputSource
import java.io.StringReader

/**
 * Content formatter
 * @author mclaren
 */
abstract class ContentFormatter[M] {
  def format(): M
}

object ContentFormatter {
  def apply(stream: Array[Byte])(contentType: String = "text/html") = contentType.contains("html") match {
    case true => HtmlContentFormatter(stream)
    case false => Array("json", "javascript").filter(contentType.contains _).isEmpty match {
      case true => JsonContentFormatter(stream)
      case _ => null
    }
  }
}

case class HtmlContentFormatter(stream: Array[Byte]) extends ContentFormatter[Document] {
  override def format(): Document = {
    var dom = new DOMParser()
    dom.parse(new InputSource(new StringReader(new String(stream))))
    dom.getDocument
  }
}

case class JsonContentFormatter(stream: Array[Byte]) extends ContentFormatter[Unit] {
  val scriptEngine = new ScriptEngineManager().getEngineByExtension("js")
  scriptEngine.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE)
  def bind(jsVariable: String, javaRef: Any): JsonContentFormatter = {
    this.scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).put(jsVariable, javaRef)
    this
  }

  def evaluate(shellcode: String): JsonContentFormatter = {
    this.scriptEngine.eval(shellcode)
    this
  }

  def bindingcodes = new String(stream)

  override def format(): Unit = {}
}