package com.ecyware.web.robot

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb._
import com.mongodb.ServerAddress
import scalaj.collection.s2j._

class RegPubStorage extends RobotStorage {
  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()
  val mongoCollection = MongoConnection()("webdata")("regpub")
  val errorLogs = MongoConnection()("webdata")("errorLogs")
  val dbCategories = MongoConnection()("webdata")("regpub_categories")
  
  def saveCategories(categories:Seq[String]) {
    
    
    val records = for (i <- categories)
    yield
    {
      Map("url" -> i,"parse" -> false)
    }
    
    records.foreach  
    {  
      record => 
        dbCategories += record.asDBObject 
    }
    
  }
  
  def completeCategory(url:String) {
    val uri = new java.net.URL(url)
    val segments = uri.getQuery.split("&")
    val code = segments(1).split("=")(1)
    val formattedUrl = RegPubParser().getUrlFromCode(code.toLowerCase)
    val cursor = dbCategories.find(MongoDBObject("url" -> formattedUrl))
    println()
    if ( cursor.hasNext ) {
      val firstItem = cursor.next
      // update parse to true 
      val id = MongoDBObject("_id" -> firstItem.get("_id"))
      dbCategories.update(id, $set("parse" -> true))
    }
  }
  def popCategory() : String = {
    // get first item
    val cursor = dbCategories.find(MongoDBObject("parse" -> false))
    if ( cursor.hasNext ) {
      val firstItem = cursor.next
      firstItem.getAs[String]("url").get
    } 
    else 
    {
      ""
    }
  }
  def saveOrUpdate(items:Map[String,Object])  {
   // webdata is database
   // regpub is collection
   val dbItems = items.asDBObject
   val existingItems = mongoCollection.find(dbItems) 
   if ( existingItems.length == 0 )
   {
     mongoCollection += dbItems
     println(String.format("Added %s item(s)", existingItems.length.toString))
   }
 }
  
    // log failures 
  def logFailure(url:String) {
    val now = System.currentTimeMillis 
    
    val errorLogItem = Map("url" -> url) ++ Map("timestamp" -> now)
    errorLogs += errorLogItem.asDBObject
  }
  
  def findCompanyUrl(url:String):Option[String] = {

    val urlParams = url.split("ID=")
    val ficha = if ( urlParams.length == 2) { urlParams(1) } else { "" }
 
    val cursor = mongoCollection.find(MongoDBObject("ficha" -> ficha))
    //val items = cursor.map( dbo => dbo.getAs[String]("ficha").get).toSeq
    
    if ( cursor.length == 1)
    {
      Option(url)
    } else {
      None
    }
  
  }
  
  /** read failures and attempt to get urls again */
  def readFailedLogs():Seq[String] = {
    Seq.empty[String]
  }
}