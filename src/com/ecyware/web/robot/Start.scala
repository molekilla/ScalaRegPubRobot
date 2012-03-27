// regpub parser 
// Rogelio Morrell
// 2012
package com.ecyware.web.robot

import com.typesafe.config._
import akka.actor._
import akka.dispatch._
import scalaj.http.Http
import org.jsoup._
import org.jsoup.nodes.Element
import scala.collection.JavaConversions._
import com.ecyware.web.robot

object Main extends App {

    //val dispatcherConf =  ConfigFactory.parseFile(new java.io.File("config/dispatch.conf"))
      val dispatcherConf = ConfigFactory.parseString("""
robotDispatcher {
  # Dispatcher is the name of the event-based dispatcher
  type = Dispatcher
  # What kind of ExecutionService to use
  executor = "fork-join-executor"
  # Configuration for the fork join pool
  fork-join-executor {
    # Min number of threads to cap factor-based parallelism number to
    parallelism-min = 2
    # Parallelism (threads) ... ceil(available processors * factor)
    parallelism-factor = 2.0
    # Max number of threads to cap factor-based parallelism number to
    parallelism-max = 12
  }
  # Throughput defines the maximum number of messages to be
  # processed per actor before the thread jumps to the next actor.
  # Set to 1 for as fair as possible.
  throughput = 20
}
      """)
    val system = ActorSystem("RobotSpace", ConfigFactory.load(dispatcherConf))
    
    val nrOfWorkers = 10
    val master = system.actorOf(Props(new Master(nrOfWorkers)), name = "master")
    
    // start the calculation
    master ! Calculate

}