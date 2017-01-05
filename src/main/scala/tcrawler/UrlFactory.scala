package tcrawler

import java.net.URL
import java.util.Hashtable
import scala.collection.JavaConversions._

case class UrlFactory(url: String) {
  var fd = new URL(url)
  var prefix = url.substring(0, url.indexOf("?"))
  var suffix = fd.getQuery
  var tb = new Hashtable[String, String]()
  suffix.split("&").map(expr => {
    var tuple = new Array[String](2)
    var cutpoint = expr.indexOf("=")
    tuple(0) = expr.substring(0, cutpoint)
    tuple(1) = expr.substring(cutpoint + 1)
    tuple
  }).map(x => (x(0), x(1))).foreach(x => tb.put(x._1, x._2))

  def read_param(paramName: String) = tb.get(paramName)
  def write_param(paramName: String, paramValue: String) = {
    tb.put(paramName, paramValue)
    this
  }

  def get = {
    var bf = new StringBuilder
    bf.append(prefix).append("?")
    tb.entrySet().foreach(entry => {
      bf.append(entry.getKey).append("=").append(entry.getValue).append("&")
    })

    bf.toString().endsWith("&") match {
      case true => bf.delete(bf.length - 1, bf.length).toString()
      case false => bf.toString()
    }
  }
}

object UrlFactory extends App {
  var url = "http://www.ebay.com/itm/PU-Leather-Magnetic-Holder-Flip-Case-Cover-For-IPhone-HTC-Samsung-Mobile-Phone-/371500073228?var=&hash=item567f21410c:m:mncAONPkxuoPWV3-a_s2tkQ"

  println(UrlFactory(url).write_param("name", "mclaren").get)
}