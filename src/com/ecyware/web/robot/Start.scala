// regpub parser 
// Rogelio Morrell
// 2012
package com.ecyware.web.robot


import akka.actor._
import scalaj.http.Http
import org.jsoup._
import org.jsoup.nodes.Element
import scala.collection.JavaConversions._
import com.ecyware.web.robot

object Main extends App {

    val system = ActorSystem("RobotSpace")
    // from 3 to 5 equals .4 to .9 in networking
    
    val nrOfWorkers = 5
    val master = system.actorOf(Props(new Master(nrOfWorkers)), name = "master")
    
    // start the calculation
    master ! Calculate

}