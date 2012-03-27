package com.ecyware.web.robot

trait ScalaJHttp extends HttpUtil {
  import scalaj.http._
  

  def readHtml(url:String)(handleFailedUrl:String=>Unit) = {
    try {
      // println("start-scalaj:" + (new java.util.Date).toString)
    val html = Http(url)
     .option(HttpOptions.connTimeout(5000))
     .option(HttpOptions.readTimeout(5000)).asString
     // println("end-scalaj:" + (new java.util.Date).toString)
     Some(html)
    }
     catch {
       case e: Exception =>
         handleFailedUrl(url)
         None
     }
  }
}

trait DispatchHttp extends HttpUtil {
  import dispatch._
  
    def readHtml(urlString:String)(handleFailedUrl:String=>Unit) = {
    try {
      
      val http = new Http
      val requestUrl = url(urlString).copy(defaultCharset = "windows-1252")
      val html = http(requestUrl as_str).toString

      Some(html)
    }
     catch {
       case e: Exception =>
         handleFailedUrl(urlString)
         None
     }
  }
}

trait HttpUtil {
    def readHtml(url:String)(handleFailedUrl:String=>Unit):Option[String]
}