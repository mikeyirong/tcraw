package tcrawler

import java.io.ByteArrayOutputStream

import JavaVirtualMachine.StringLike
import org.apache.commons.httpclient.SimpleHttpConnectionManager
import org.apache.commons.httpclient.params.HttpClientParams
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.DefaultMethodRetryHandler
import java.nio.charset.Charset
import org.apache.commons.httpclient.Header
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.NameValuePair

/**
 * Communication channel of Internet I/O
 * @author mclaren
 */
object InternetIO extends LoggingSupport {
  /**
   * Returns byte stream from the specified URL which was via HTTP POST
   */
  def postUrl(url: String, headers: Array[Header] = Array[Header]())(body: Array[NameValuePair]): String = {
    var post: PostMethod = null
    try {
      val client = new HttpClient(new HttpClientParams, new SimpleHttpConnectionManager(true))

      client.getHttpConnectionManager.getParams.setConnectionTimeout(15000)
      client.getHttpConnectionManager.getParams.setSoTimeout(15000)
      var retryHandler = new DefaultMethodRetryHandler
      retryHandler.setRetryCount(1)
      retryHandler.setRequestSentRetryEnabled(false)

      post = new PostMethod(url)
      post.setMethodRetryHandler(retryHandler)
      post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")

      post.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

      post.setRequestHeader("Accept-Language", "en-US,en;q=0.5")

      post.setRequestHeader("Connection", "keep-alive")

      //      post.setRequestHeader("cookie", "_s_tentry=-; Apache=4924724074098.557.1467187917778; SINAGLOBAL=4924724074098.557.1467187917778; ULV=1467187917809:1:1:1:4924724074098.557.1467187917778:; YF-Ugrow-G0=3a02f95fa8b3c9dc73c74bc9f2ca4fc6; WBStore=8ca40a3ef06ad7b2|undefined; YF-Page-G0=b9004652c3bb1711215bacc0d9b6f2b5; WBtopGlobal_register_version=62a51516a4a355e3; SUB=_2A256d_NfDeTxGeRJ6VsV9C_EwzmIHXVZBWOXrDV8PUNbmtBeLVH_kW91nue9KvaW5LSwDd8UlbSV7kq2Ww..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhjvWiKQ765l6oqYHorSH-p5JpX5K2hUgL.FozNeo.XSh2R1h-2dJLoIEqLxK-L1hnL1KnLxK.L1K.L1hnLxKBLB.2LB--_eoME1KBXeKnRSBtt; SUHB=0TPkIVZpcrxwNG; ALF=1467792778; SSOLoginState=1467187983; un=18676731013; wvr=6; YF-V5-G0=7fb6f47dfff7c4352ece66bba44a6e5a; wb_bub_hot_2729441885=1")

      post.setRequestHeader("Upgrade-Insecure-Requests", "1");
      headers.foreach(post.setRequestHeader _)

      //设置POST请求体
      post.setRequestBody(body)
      var code = client.executeMethod(post)

      //重定向
      if (code == 302) {
        var directUrl = post.getResponseHeader("Location").getValue

        fromUrl(directUrl, post.getResponseHeaders)
      } else {
        var encoding = getencoding(post.getResponseHeader("Content-Type").getValue, Charset.defaultCharset().name())

        var inputstream = post.getResponseBodyAsStream
        var out = new ByteArrayOutputStream
        var i = 0
        var bytes = new Array[Byte](1024)

        do {
          i = inputstream.read(bytes)
          if (i != -1)
            out.write(bytes, 0, i)
        } while (i != -1)

        //        println("RS" + new String(out.toByteArray()))
        var s = new String(out.toByteArray(), encoding)
        s
      }
    } finally {
      if (post != null) post.releaseConnection();
    }
  }
  /**
   * Returns byte stream from the specified URL
   */
  def fromUrl(url: String, headers: Array[Header] = Array[Header]()): String = {
    var get: GetMethod = null
    try {
      logger.info("Fetch url {}", url)
      val client = new HttpClient(new HttpClientParams, new SimpleHttpConnectionManager(true))

      client.getHttpConnectionManager.getParams.setConnectionTimeout(15000)
      client.getHttpConnectionManager.getParams.setSoTimeout(15000)
      var retryHandler = new DefaultMethodRetryHandler
      retryHandler.setRetryCount(1)
      retryHandler.setRequestSentRetryEnabled(false)

      get = new GetMethod(url)
      get.setMethodRetryHandler(retryHandler)
      get.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")

      get.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

      get.setRequestHeader("Accept-Language", "en-US,en;q=0.5")

      get.setRequestHeader("Connection", "keep-alive")

      get.setRequestHeader("cookie", "_s_tentry=-; Apache=4924724074098.557.1467187917778; SINAGLOBAL=4924724074098.557.1467187917778; ULV=1467187917809:1:1:1:4924724074098.557.1467187917778:; YF-Ugrow-G0=3a02f95fa8b3c9dc73c74bc9f2ca4fc6; WBStore=8ca40a3ef06ad7b2|undefined; YF-Page-G0=b9004652c3bb1711215bacc0d9b6f2b5; WBtopGlobal_register_version=62a51516a4a355e3; SUB=_2A256d_NfDeTxGeRJ6VsV9C_EwzmIHXVZBWOXrDV8PUNbmtBeLVH_kW91nue9KvaW5LSwDd8UlbSV7kq2Ww..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhjvWiKQ765l6oqYHorSH-p5JpX5K2hUgL.FozNeo.XSh2R1h-2dJLoIEqLxK-L1hnL1KnLxK.L1K.L1hnLxKBLB.2LB--_eoME1KBXeKnRSBtt; SUHB=0TPkIVZpcrxwNG; ALF=1467792778; SSOLoginState=1467187983; un=18676731013; wvr=6; YF-V5-G0=7fb6f47dfff7c4352ece66bba44a6e5a; wb_bub_hot_2729441885=1")

      get.setRequestHeader("Upgrade-Insecure-Requests", "1");
      headers.foreach(get.setRequestHeader _)

      var code = client.executeMethod(get)

      //重定向
      if (code == 302) {
        var directUrl = get.getResponseHeader("Location").getValue

        fromUrl(directUrl, get.getResponseHeaders)
      } else {
        var encoding = getencoding(get.getResponseHeader("Content-Type").getValue, Charset.defaultCharset().name())

        var inputstream = get.getResponseBodyAsStream
        var out = new ByteArrayOutputStream
        var i = 0
        var bytes = new Array[Byte](1024)

        do {
          i = inputstream.read(bytes)
          if (i != -1)
            out.write(bytes, 0, i)
        } while (i != -1)

        var s = new String(out.toByteArray(), encoding)
        s
      }
    } finally {
      if (get != null) get.releaseConnection();
    }
  }

  private[tcrawler] def getencoding(contentType: String, default: String): String = {
    try {
      var cs = default
      contentType.split(";").foreach(part => {
        if (part.toUpperCase().trim().startsWith("CHARSET")) cs = part.split("=")(1).trim
      })
      cs
    } catch {
      case e: Exception => default
    }
  }
}