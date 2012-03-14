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
        val list = for (tr <- trs) 
        yield
        {
            val tds = tr.toTableCellsSeq
            tds.get(0).text.trim -> tds.get(1).text.trim
        }

        list.toMap
    }
    
        
    def getDirectors(): com.mongodb.BasicDBList = {
        
        val cells = seq.get(26).toTableCellsSeq.filter( td => td.hasText).map( td => td.text.trim)

        val builder = com.mongodb.casbah.commons.MongoDBList.newBuilder
        for ( item <- cells  )
        {
          builder += item
        }
        builder.result
    }
    
    def getSubscribers(): com.mongodb.BasicDBList = {
        val cells = seq.get(28).toTableCellsSeq.filter( td => td.hasText).map( td => td.text.trim)

        val builder = com.mongodb.casbah.commons.MongoDBList.newBuilder
        for ( item <- cells  )
        {
          builder += item
        }
        builder.result
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