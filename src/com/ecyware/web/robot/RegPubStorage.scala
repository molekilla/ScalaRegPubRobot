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
  
  def saveOrUpdate(items:Map[String,Object]) = {
   // webdata is database
   // regpub is collection
   val dbItems = items.asDBObject
   val existingItems = mongoCollection.find(dbItems) 
   if ( existingItems.length == 0 )
   {
     mongoCollection += dbItems
     println(String.format("Added %s items", existingItems.length.toString))
   } else {
     // update
     println("Should update if existing")
     // mongoCollection.update(existingItems)
   }
 }
  
    // log failures 
  def logFailure(url:String) {
    val now = System.currentTimeMillis 
    
    val errorLogItem = Map("url" -> url) ++ Map("timestamp" -> now)
    errorLogs += errorLogItem.asDBObject
  }
  
  // read failures and attempt to get urls again
  def readFailedLogs():Seq[String] = {
    Seq.empty[String]
  }
}