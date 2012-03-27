package com.ecyware.web.robot

import com.mongodb._
import com.mongodb.util._
import com.mongodb.casbah.commons.conversions.scala._
import org.elasticsearch.action.index._
import org.elasticsearch.common.settings._
import org.elasticsearch.client.transport._
import org.elasticsearch.common.transport.InetSocketTransportAddress


trait IndexerUtil {

  def indexRegPubDocument(document:com.mongodb.DBObject, ficha:String)
}

trait ElasticSearchIndexer extends IndexerUtil {
  val settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "elasticsearch").build()
                
  val address = new InetSocketTransportAddress("localhost",9300)
  val client = new TransportClient(settings)
  .addTransportAddress(address)

  def indexRegPubDocument(document:com.mongodb.DBObject, ficha:String) {
    val indexRequest = new IndexRequest("regpub","sa", ficha)
    val json = JSON.serialize(document)
    indexRequest.source(json);
    val response = client.index(indexRequest).actionGet();
  }
}