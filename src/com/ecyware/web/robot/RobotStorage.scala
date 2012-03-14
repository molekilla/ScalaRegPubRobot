package com.ecyware.web.robot

trait RobotStorage {

  /** Save or update record */
  def saveOrUpdate(items:Map[String,Object])
  
  /** Finds company url item */
  def findCompanyUrl(url:String):Option[String]
  
  
  /** log failures  */
  def logFailure(url:String)
  
  // close failure
  // def closeLogFailure(url:String)
  
  /** read failures and attempt to get urls again */
  def readFailedLogs():Seq[String]
  
  def saveCategories(categories:Seq[String])

  def popCategory():String
  def completeCategory(url:String)
}