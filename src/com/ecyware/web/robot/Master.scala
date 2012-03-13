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

class Master(nrOfWorkers: Int) extends Actor {
    


val categories = RegPubParser().getCategoryUrl()
val router = context.actorOf(Props[Worker]
.withRouter(RoundRobinRouter(nrOfWorkers)
.withDispatcher("my-dispatcher")), name = "workerRouter")
//.withDispatcher("my-dispatcher")

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
      router ! Scrap(nextPageUrl, Category)
       
    
  case Calculate =>  
    categories.foreach(router ! Scrap(_, Category))

  case Store(parent, dataItems) =>
    println(dataItems.mkString)
    ComponentRegistry.robotStorage.saveOrUpdate(dataItems)
}

}