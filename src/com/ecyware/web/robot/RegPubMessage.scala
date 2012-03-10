package com.ecyware.web.robot

import scala.collection.JavaConversions._
import java.util._

trait RegPubSequence
case object Category extends RegPubSequence
case object CategorySingle extends RegPubSequence
case object Company extends RegPubSequence

trait RegPubMessage

case object Calculate extends RegPubMessage
case class Scrap(url:String, level:RegPubSequence) extends RegPubMessage
case class CategoryData(links:Seq[String], hasNextPage:Option[String]) extends RegPubMessage
case class Store(dataItems:scala.collection.immutable.Map[String, Object]) extends RegPubMessage