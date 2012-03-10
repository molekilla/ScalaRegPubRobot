package com.ecyware.web.robot

object ComponentRegistry extends RobotStorageComponent
{
  val robotStorage = new RegPubStorage()
}

trait RobotStorageComponent {

  val robotStorage: RobotStorage
}