package com.ecyware.web.robot

import org.jsoup._
import org.jsoup.nodes.Element
import scala.collection.JavaConversions._
import JSoupExtensions._

object CompanyPageMappings {

  implicit def jsoupToMappings(seq:Seq[org.jsoup.nodes.Element]) = new {
    def getDignatarios(): Map[String, String] = {
        val trs = seq.get(24).toTableRowsSeq.filter( 
            tr => tr.toTableCellsSeq.get(0).text.trim.length > 0)
    
        val builder = Map.newBuilder[String, String]
 
        for (tr <- trs) 
        {
            val tds = tr.toTableCellsSeq
            builder += ("dignatario" -> tds.get(0).text.trim)
            builder += ( "nombre" -> tds.get(1).text.trim)
        }

        builder.result
    }
    
        
    def getDirectors(): Map[String, String] = {
        val cells = seq.get(26).toTableCellsSeq.filter(td => td.text.trim.length > 0 )
    
        val list =
        for (td <- cells)
        yield 
        {
            "nombre" -> td.text.trim
        }

        list.toMap
    }
    
    def getSubscribers(): Map[String, String] = {
        val cells = seq.get(28).toTableCellsSeq.filter(td => td.text.trim.length > 0 )
    
        val list =
        for (td <- cells)
        yield 
        {
            "nombre" -> td.text.trim
        }

        list.toMap
    }
    
    def getMicro(): Map[String, String] = {
      val microTable = seq.get(16).toTableCellsSeq
      
      val builder = Map.newBuilder[String, String]
      builder += "microRollo" -> microTable.get(1).text.trim
      builder += "microImagen" -> microTable.get(3).text.trim
      builder += "microMoneda" -> seq.get(17).toTableCellsSeq().get(1).text.trim
      builder += "microMontoCapital" -> seq.get(18).toTableCellsSeq().get(1).text.trim
      builder += "microCapitalTexto" -> seq.get(20).text.trim
      builder += "microRepresentanteLegal" -> seq.get(22).text.trim
      
      builder.result
    }
    
  }
  

}