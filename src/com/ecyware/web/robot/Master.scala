// regpub parser 
// Rogelio Morrell
// 2012
package com.ecyware.web.robot


import akka.dispatch._
import akka.actor.{Actor, PoisonPill}
import akka.actor._
import akka.dispatch.Dispatchers
import akka.routing.RoundRobinRouter
import akka.dispatch.Future
import akka.pattern._
import akka.util.Timeout
import akka.util.duration._
import akka.dispatch.{ ExecutionContext, Promise }

class Master(nrOfWorkers: Int) extends Actor {
    


// Note this is just a test of 3 categories
val categories = RegPubParser().getCategoryUrl().take(3)
val categoryCount = categories.length    
val router = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

implicit val ec = ExecutionContext.defaultExecutionContext(context.system)
implicit val timeout = Timeout(10 seconds)
    
def receive = {

  case CategoryData(links, hasNextPage) =>

    links.foreach( url => (router ! Scrap(url, Company) ))
//    val scrapCompany = links.map( url => (router ? Scrap(url, Company) ).mapTo[Store] )
//    val futureResult = Future.sequence(scrapCompany)
//    futureResult onComplete {
//      case Right(items) =>
//        println(items.length)
//      case Left(failure) =>
//        println(failure)
//    }
//    futureResult.pipeTo(router)

    val nextPageUrl = hasNextPage.getOrElse("")
    if ( nextPageUrl != "")
      router ! Scrap(nextPageUrl, Category)
       
    
  case Calculate =>  
    categories.foreach(router ! Scrap(_, Category))

  case Store(dataItems) =>
    println(dataItems.mkString)
    ComponentRegistry.robotStorage.saveOrUpdate(dataItems)
}

}