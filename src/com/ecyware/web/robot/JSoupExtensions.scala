package com.ecyware.web.robot

import org.jsoup._
import org.jsoup.nodes.Element
import scala.collection.JavaConversions._

object JSoupExtensions {

  implicit def jsoupSelectToSeq(jsoupSelect:org.jsoup.select.Elements) = new {
    def toSeq(): Seq[org.jsoup.nodes.Element] = {
      jsoupSelect.subList(0, jsoupSelect.size)
    }
  }
  
    implicit def jsoupSelectTableCellsToSeq(el:org.jsoup.nodes.Element) = new {
    def toTableCellsSeq(): Seq[org.jsoup.nodes.Element] = {
      val items = el.select("td")
      items.subList(0, items.size)
     }
       
    def toTableRowsSeq(): Seq[org.jsoup.nodes.Element] = {
      val items = el.select("tr")
      items.subList(0, items.size)
     }
    }
}