Scala RegPub Robot
======================================

This is a web parser for Registro Publico de Panama to look for companies registered (useful for any newspaper).

At a glance
----------------------


+ Using Akka 2.0 in Master/Worker classs
+ Pimp My Library in JSoupExtensions
* Cake Pattern, which I'm not sure is the correct implementation, for the Storage trait
* jSoup for HTML parsing
* Have a trait for either ScalaJ or Dispatch. Using ScalaJ because Dispatch requires to set the correct charset
* MongoDB for storage
* Java wrapper for creting runnable jar

Take note that this is not using SBT , which I tried but is failing to work properly. 

##Libraries
* Akka 2.0
* Casbah commons, core and query
* Commons logging
* Dispatch core and http
* Http Client and Core (Dispatch dependencies)
* Joda Time (MongoDB dependency)
* jSoup
* MongoDB 2.7.3 and Driver
* ScalaJ collection and http
* SF4LJ (MongoDB dependency)