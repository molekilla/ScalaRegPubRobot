// regpub parser 
// Rogelio Morrell
// 2012
package com.ecyware.web.robot
import akka.actor._

class Worker extends Actor with akka.actor.ActorLogging {
  
    
  def receive = {
    case Scrap(url, level) if level == Category   =>
      sender ! getCategoryData(parse(url)) 
    case Scrap(url, level) if level == Company   =>
      sender ! getCompanyData(parse(url), url)
  }
  
  // parses url and returns string
  def parse(url:String) = {
    RegPubParser().readHtml(url) {
        urlError =>
          ComponentRegistry.robotStorage.logFailure(urlError)
      }
  }
  
  // should scrap data to a database, for now prints and returns url
  def getCompanyData(html:Option[String], url:String) = {
    log.info("url: " + url)
    val result = RegPubParser().parseCompany(url, html.getOrElse(""))
    result
  }
  
  // returns a CategoryData message
  def getCategoryData(html: Option[String]) = {
    if ( html.isDefined ){ 
        RegPubParser().parseCategory(html.get)
    } else {
      CategoryData
    }
  }
}