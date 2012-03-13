// regpub parser 
// Rogelio Morrell
// 2012
package com.ecyware.web.robot
import akka.actor._

class Worker extends Actor with akka.actor.ActorLogging with ScalaJHttp {
  
    
  def receive = {
    case Scrap(url, level) 
    if level == Category && url.length > 0  =>
      sender ! getCategoryData(url) 
    case Scrap(url, level) 
    if level == Company && url.length > 0  =>
      sender ! getCompanyData(url)
  }
  
 
  def parse(url:String) = {
    readHtml(url) {
        urlError =>
          ComponentRegistry.robotStorage.logFailure(urlError)
      }
  }
  
  def getCompanyData( url:String) = {
    log.info("url: " + url)
    val html = parse(url)
    RegPubParser().parseCompany(url, html.getOrElse(""))
  }
  

  def getCategoryData(url:String) = {
    val html = parse(url)
    RegPubParser().parseCategory(url, html.getOrElse(""))
//    if ( html.isDefined ){ 
//        
//    } else {
//      CategoryData
//    }
  }
}