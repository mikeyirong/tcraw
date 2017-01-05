package tcrawler

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.Element

/**
 * @author Administrator
 *
 */
/**
 * JQUERY 兼容库
 *
 * @author mclaren
 *
 */
class JQ(nd: List[Node], init: JQ => Unit) extends JQSupport with StringAdapter {
  /**
   * DOM对象
   */
  var dom = nd.filter(x => x != null)

  /**
   * 执行初始化
   */
  init(this)

  def this(nd: List[Node]) = this(nd, (jq: JQ) => {})

  def this(nd: Node, init: JQ => Unit) = this(List(nd), init)
  /**
   * 过滤器
   */
  @deprecated
  def filter(expr: String): List[JQ] = {
    if (expr.startsWith("#")) {
      //ID查询
      List[JQ]($(dom2Document(this.dom(0)).getElementById(expr.substring(1).split(" ")(0))))
    } else if (expr.startsWith(".")) {
      //CLASS查询
      var elements = List[Node]()
      this.dom.foreach(nd => elements = elementsbyTagname(nd, expr.split("->")(1).substring(1).trim(), expr.split("->")(0).substring(1).trim()) ::: elements)

      var rts = List[JQ]()

      elements.foreach { nd => rts = $(nd) :: rts }
      rts
    } else {
      //TAG查询
      var elements = List[Node]()
      this.dom.foreach { nd => elements = elementsbyTagname(nd, expr, null) ::: elements }
      var rts = List[JQ]()

      elements.foreach { nd => rts = $(nd) :: rts }
      rts
    }
  }

  /**
   * 选择器
   */
  def select(expr: String): List[JQ] = {
    if (expr.startsWith("#")) {
      //ID查询
      List[JQ]($(dom2Document(this.dom(0)).getElementById(expr.substring(1).split(" ")(0))))
    } else if (expr.startsWith(".")) {
      //CLASS查询
      var elements = List[Node]()
      this.dom.foreach(nd => elements = elementsbyTagname(nd, expr.split("->")(1).substring(1).trim(), expr.split("->")(0).substring(1).trim()) ::: elements)

      var rts = List[JQ]()

      elements.foreach { nd => rts = $(nd) :: rts }
      rts
    } else {
      //TAG查询
      var elements = List[Node]()
      this.dom.foreach { nd => elements = elementsbyTagname(nd, expr, null) ::: elements }
      var rts = List[JQ]()

      elements.foreach { nd => rts = $(nd) :: rts }
      rts
    }
  }

  /**
   * 获取一个JQ对象
   */
  def first(): JQ = if (this.dom.isEmpty) this else new JQ(List[Node](this.dom(0)))

  /**
   * 获取第一个DOM对象
   */
  def firstDom(f: List[Node] => Node): Node = f(first().dom)

  /**
   * 获取同级下一个JQ对象
   */
  def next(): JQ = $(this.dom2element(this.dom(0)).getNextSibling())

  /**
   * 获取同级前一个JQ对象
   */
  def prev(): JQ = $(this.dom2element(this.dom(0)).getPreviousSibling())

  /**
   * 获取第一个子JQ对象
   */
  def firstChild(): JQ = $(this.dom2element(this.dom(0)).getFirstChild())

  /**
   * 获取最后一个个子JQ对象
   */
  def lastChild(): JQ = $(this.dom2element(this.dom(0)).getLastChild())

  /**
   * 获取节点属性
   */
  def attr(attrName: String): String = {
    ((nd: Node) => {
      nd match {
        case e: Element => e.getAttribute(attrName)
        case _ => ""
      }
    })(firstDom(nds => if (nds.isEmpty) null else nds(0)))
  }

  /**
   * 获取节点文本内容
   */
  def text(): String = {
    var texts = List[String]()
    this.dom.foreach { n => texts = ((t: String) => { if (isEmpty(t)) "" else t.trim })(n.getTextContent) :: texts }
    texts.mkString
  }

  /**
   * 通过Tag name以及class name进行匹配
   */
  def elementsbyTagname(that: Node, tagname: String, classname: String): List[Node] = {
    val children = (() => {
      if (isEmpty(tagname)) {
        that.getChildNodes
      } else {
        that match {
          case e: Element => e.getElementsByTagName(tagname)
          case d: Document => d.getElementsByTagName(tagname)
          case _ => throw new IllegalArgumentException("DOM类型错误!")
        }
      }
    })()

    var matches = List[Node]()
    for (i <- 0 until children.getLength) {
      matches = children.item(i) :: matches
    }

    if (isEmpty(classname)) matches else
      matches.filter(node => {
        node match {
          case e: Element => equal(classname, e.getAttribute("class"))
          case _ => false
        }
      })
  }

  /**
   * DOM NODE 转换 ELEMENT
   */
  def dom2element(nd: Node): Element = {
    nd match {
      case m: Element => m
      case _ => throw new ClassCastException("需要类型:org.w3c.Element，但实际为:" + nd.getClass)
    }
  }

  def dom2Document(nd: Node): Document = {
    nd match {
      case d: Document => d
      case _ => throw new ClassCastException("需要类型:org.w3c.Document,但实际为:" + nd.getClass)
    }
  }
}

trait JQSupport {
  def $(nd: Node): JQ = new JQ(nd, (jq: JQ) => {})
}