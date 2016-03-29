package com.databricks.apps.twitter_classifier

import java.io.File

import com.google.gson.{Gson,GsonBuilder, JsonParser}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import com.mongodb.casbah.Imports._

import com.mongodb.QueryBuilder
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}


/**
 * Collect at least the specified number of tweets into json text files, cassandra, mongo...

on mongo shell:

 use highschool;
 db.tweets.find();
 */
object CollectWithMongo {

  private var numTweetsCollected = 0L
  private var partNum = 0
  
  private val Database = "highschool"
  private val Collection = "tweets"
  private val MongoHost = "127.0.0.1"
  private val MongoPort = 27017
  private val MongoProvider = "com.stratio.datasource.mongodb"

  private val jsonParser = new JsonParser()
  private val gson = new GsonBuilder().setPrettyPrinting().create()

  private def prepareMongoEnvironment(): MongoClient = {
      val mongoClient = MongoClient(MongoHost, MongoPort)
      //populateMongoTable(mongoClient)
      mongoClient
    }

  private def cleanMongoEnvironment(mongoClient: MongoClient) = {
      cleanMongoData(mongoClient)
      mongoClient.close()
    }

  private def populateMongoTable(client: MongoClient): Unit = {

      val collection = client(Database)(Collection)
      for (a <- 1 to 10) {
        collection.insert {
          MongoDBObject("id" -> a.toString,
            "age" -> (10 + a),
            "description" -> s"description $a",
            "enrolled" -> (a % 2 == 0),
            "name" -> s"Name $a"
          )//MongoDBObject
        }//collection.insert
      }//for

      collection.update(QueryBuilder.start("age").greaterThan(14).get, MongoDBObject(("$set", MongoDBObject(("optionalField", true)))), multi = true)
      collection.update(QueryBuilder.start("age").is(14).get, MongoDBObject(("$set", MongoDBObject(("fieldWithSubDoc", MongoDBObject(("subDoc", MongoDBList("foo", "bar"))))))))
  }

  private def cleanMongoData(client: MongoClient): Unit = {
      val collection = client(Database)(Collection)
      collection.dropCollection()
  }

  def main(args: Array[String]) {
    // Process program arguments and set properties
    if (args.length < 2) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        "<numTweetsToCollect> <intervalInSeconds> <partitionsEachInterval>")
      System.exit(1)
    }
    val Array(Utils.IntParam(numTweetsToCollect),  Utils.IntParam(intervalSecs), Utils.IntParam(partitionsEachInterval)) =Utils.parseCommandLineWithTwitterCredentials(args)

    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[4]")
    val sc = new SparkContext(conf)
    //i want to reuse this in sqlContext
    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth).map(gson.toJson(_))
    
    println("Initialized Streaming Spark Context.")  

    println("Initializing mongodb connector...")

    val mongoClient = prepareMongoEnvironment()
    val collection = mongoClient(Database)(Collection)
    
    println("Initialized mongodb connector...")

    try {
        val sqlContext = new SQLContext(sc)
        println("Creating temporary table in mongo instance...")
        sqlContext.sql(
            s"""|CREATE TEMPORARY TABLE $Collection
              |(id STRING, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)
              |USING $MongoProvider
              |OPTIONS (
              |host '$MongoHost:$MongoPort',
              |database '$Database',
              |collection '$Collection'
              |)
            """.stripMargin.replaceAll("\n", " "))

        tweetStream.foreachRDD(rdd => {
          val count = rdd.count()
          if (count>0) {
            val topList = rdd.take(count.toInt)
            println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
            //topList.foreach(println)
            //println

            for (tweet <- topList) {
              val atweet = gson.toJson(jsonParser.parse(tweet))
              //println("a tweet... " + atweet)
              //println
              collection.insert {
                MongoDBObject("id" -> count.toString,
                "age" -> (10 + count),
                "description" -> s"description $atweet",
                "enrolled" -> (count % 2 == 0),
                "name" -> s"Name $count"
                )//MongoDBObject
              }

            }//for (tweet <- topList)
            numTweetsCollected += count
            if (numTweetsCollected > numTweetsToCollect) {
              println
              println("numTweetsCollected > numTweetsToCollect condition is reached. Stopping..." + numTweetsCollected + " " + count)
              //cleanMongoEnvironment(mongoClient)
              println("shutdown mongodb connector...")
              System.exit(0)
            }
          }//if(count>0)
        })//tweetStream.foreachRDD(rdd =>
        val studentsDF = sqlContext.read.format("com.stratio.datasource.mongodb").table(s"$Collection")
        studentsDF.where(studentsDF("age") > 15).groupBy(studentsDF("enrolled")).agg(avg("age"), max("age")).show(5)
        println("tested a mongodb connection with stratio library...")
    } finally {
        //sc.stop()
        println("finished withSQLContext...")
    }

    

/*
    tweetStream.foreachRDD((rdd, time) => {
      val count = rdd.count()
      if (count > 0) {
        val outputRDD = rdd.repartition(partitionsEachInterval)
        outputRDD.saveAsTextFile(outputDirectory + "/tweets_" + time.milliseconds.toString)
        numTweetsCollected += count
        if (numTweetsCollected > numTweetsToCollect) {
          println
          println("numTweetsCollected > numTweetsToCollect condition is reached. Stopping..." + numTweetsCollected + " " + count)
          System.exit(0)
        }
      }
    })
*/
    ssc.start()
    ssc.awaitTermination()

    println("Finished!")
  }
}
