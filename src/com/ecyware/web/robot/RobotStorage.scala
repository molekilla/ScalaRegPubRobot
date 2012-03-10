package com.ecyware.web.robot

trait RobotStorage {

  // Save or update record
  def saveOrUpdate(items:Map[String,Object])
  
  // log failures 
  def logFailure(url:String)
  
  // close failure
  // def closeLogFailure(url:String)
  
  // read failures and attempt to get urls again
  def readFailedLogs():Seq[String]
}