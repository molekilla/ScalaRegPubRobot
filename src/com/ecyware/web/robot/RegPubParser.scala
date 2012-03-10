package com.ecyware.web.robot


import org.jsoup._
import org.jsoup.nodes.Element
import scala.collection.JavaConversions._
import JSoupExtensions._
import CompanyPageMappings._

object RegPubParser  {
  def apply() = new RegPubParser()
}
class RegPubParser extends ScalaJHttp {

  val Letters:String = "abcdefghijklmnoprqstuvxyz0123456789"
  val SaDomainUrl:String = "https://www.registro-publico.gob.pa/scripts/nwwisapi.dll/conweb/";
  val SaUrl:String = "https://www.registro-publico.gob.pa/scripts/nwwisapi.dll/conweb/MESAMENU?" +
        "TODO=MER4&FROM=%s&TO=&YN=&START=%s";

  
  // For each page until end
  val pageInit = "1"

  def getCategoryUrl() =  {   
    for (i <- Letters; j <- Letters; k <- Letters)
    yield
    {
      val searchCode = i.toString + j.toString + k.toString
      SaUrl.format(searchCode, pageInit)
    }
  }
 

  
  // parses links and next page into CategoryData response
  def parseCategory(html:String):CategoryData = {
    try { 
    val document = Jsoup.parse(html)
   
    // get links
    val anchors = document.select("a").toSeq
    val companies = anchors
    .filter(e => e.attr("href").contains("ID=") && e.attr("href").contains("TODO=SHOW") )
    .map(e => SaDomainUrl + e.attr("href"))
    
    val nextPageSequence = anchors.filter(e => e.text.toLowerCase.trim == "siguiente" )
    .first.attr("href")
   
    val hasNoNextPage = (nextPageSequence.lastIndexOf("START=") + 6 - nextPageSequence.length) == 0
    if ( hasNoNextPage )
    {
      // End 
      CategoryData(companies, None)
    } else {
      val nextPage = SaDomainUrl + nextPageSequence
      println(nextPage)
      CategoryData(companies, Some(nextPage))
    }
    }
    catch  {
      case e: Exception =>
          //  Log
          val empty = Seq.empty[String]
          CategoryData(empty, None)
    }
  }
  
  // parses company pages
  def parseCompany(url:String, html:String):Store =
  {
    if ( url.endsWith("&ID=0"))
    {
      Store(Map.empty[String, String])
    }
    try {
    val document = Jsoup.parse(html)
    val page = document.select("div[id=CONSULTA]").select("table[border=0]").toSeq()
    
    // Ficha y Documento
    val fichaDocumentoTable = page.get(0).toTableCellsSeq 
    var nombre = page.get(2).toTableCellsSeq
    val tomoTable = page.get(3).toTableCellsSeq
    val fechaRegistroTable = page.get(4).toTableCellsSeq
    val escrituraTable = page.get(5).toTableCellsSeq
    val notariaTable = page.get(6).toTableCellsSeq
    val provinciaNotariaTable = page.get(7).toTableCellsSeq
    val domicilioTable = page.get(8).toTableCellsSeq
    val prendaTable = page.get(9).toTableCellsSeq
    val tasaUnicaTable = page.get(11).toTableCellsSeq
    val tasaUnicaAgenteResidente = page.get(12).toTableCellsSeq().get(1).text.trim
    val diarioTable = page.get(14).toTableCellsSeq()
    

    val idMap = Map(
        "ficha" -> fichaDocumentoTable.get(1).text.trim,
        "documento" -> fichaDocumentoTable.get(3).text.trim,
        "nombre" -> nombre.get(0).text.trim,
        "tomo" -> tomoTable.get(1).text.trim,
        "folio" -> tomoTable.get(3).text.trim,
        "asiento" -> tomoTable.get(5).text.trim,
        "fechaRegistro" ->  fechaRegistroTable.get(1).text.trim,
        "estado" -> fechaRegistroTable.get(3).text.trim,
        "fechaEscritura" -> escrituraTable.get(3).text.trim,
        "escritura" -> escrituraTable.get(1).text.trim,
        "notaria" -> (notariaTable.get(1).text.trim + " " + notariaTable.get(2).text.trim),
        "provinciaNotaria" -> provinciaNotariaTable.get(1).text.trim,
        "duracion" -> domicilioTable.get(1).text.trim,
        "domicilio" -> domicilioTable.get(3).text.trim,
        "prenda" -> (prendaTable.get(1).text.trim() + prendaTable.get(2).text.trim),
        "tasaUnicaBoleta" -> tasaUnicaTable.get(1).text.trim,
        "tasaUnicaFechaPago" -> tasaUnicaTable.get(3).text.trim,
        "tasaUnicaAgenteResidente" -> tasaUnicaAgenteResidente,
        "diarioTomo" -> diarioTable.get(1).text.trim,
        "diarioAsiento" -> diarioTable.get(3).text.trim,
        "dignatarios" -> page.getDignatarios,
        "subscriptores" -> page.getSubscribers,
        "directores" -> page.getDirectors
        ) ++ page.getMicro
    Store(idMap)
    }
    catch {
      case e: Exception =>
        Store(Map.empty[String, String])
    }

  }

}