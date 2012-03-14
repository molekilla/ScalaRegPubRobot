// regpub parser 
// Rogelio Morrell
// 2012
package com.ecyware.web.robot


import akka.dispatch._
import akka.actor.{Actor, PoisonPill}
import akka.actor._
import akka.dispatch.Dispatchers
import akka.routing._
import akka.dispatch.Future
import akka.pattern._
import akka.util.Timeout
import akka.util.duration._
import akka.dispatch.{ ExecutionContext, Promise }

class Master(nrOfWorkers: Int) extends Actor with akka.actor.ActorLogging {
    

val router = context.actorOf(Props[Worker]
.withRouter(RoundRobinRouter(nrOfWorkers)
.withDispatcher("robotDispatcher")), name = "workerRouter")


implicit val ec = ExecutionContext.defaultExecutionContext(context.system)
implicit val timeout = Timeout(8 seconds)

    
def receive = {

  case CategoryData(parent, links, hasNextPage) =>

    links.foreach{
      url => 
        if (ComponentRegistry.robotStorage.findCompanyUrl(url) == None )
          router ! Scrap(url, Company)
    }


    val nextPageUrl = hasNextPage.getOrElse("")
    if ( nextPageUrl != "")
    {
      router ! Scrap(nextPageUrl, Category)
    }
    else {
      log.info("category: " + parent)
      ComponentRegistry.robotStorage.completeCategory(parent)
      
      val company = ComponentRegistry.robotStorage.popCategory
      router ! Scrap(company, Category)
    }
       
    
  case Calculate =>  
    //categories.foreach(router ! Scrap(_, Category))
    val categories = RegPubParser().getCategoryUrl()
    ComponentRegistry.robotStorage.saveCategories(categories)
    val company = ComponentRegistry.robotStorage.popCategory
    router ! Scrap(company, Category)

  case Store(parent, dataItems) =>
    log.info(dataItems.mkString)
    ComponentRegistry.robotStorage.saveOrUpdate(dataItems)
}

}