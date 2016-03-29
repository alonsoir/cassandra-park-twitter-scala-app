package com.databricks.apps.twitter_classifier

import java.io.File

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import common.utils.cassandra._
/**
 * Collect at least the specified number of tweets into json text files.
 */
object CollectWithCassandra {
  private var numTweetsCollected = 0L
  private var partNum = 0
  private var gson = new Gson()

  def main(args: Array[String]) {
    // Process program arguments and set properties
    if (args.length < 3) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        "<outputDirectory> <numTweetsToCollect> <intervalInSeconds> <partitionsEachInterval>")
      System.exit(1)
    }
    val Array(outputDirectory, Utils.IntParam(numTweetsToCollect),  Utils.IntParam(intervalSecs), Utils.IntParam(partitionsEachInterval)) =
      Utils.parseCommandLineWithTwitterCredentials(args)
    val outputDir = new File(outputDirectory.toString)
    if (outputDir.exists()) {
      System.err.println("ERROR - %s already exists: delete or specify another directory".format(
        outputDirectory))
      System.exit(1)
    }
    outputDir.mkdirs()

    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[4]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth)
      .map(gson.toJson(_))
    
    println("Initialized Streaming Spark Context.")  

    println("Initializing Cassandra...")
    val uri = CassandraConnectionUri("cassandra://localhost:9042/test")
    val session = Helper.createSessionAndInitKeyspace(uri)
    println("You have a open Cassandra session...")
   //   session.execute("CREATE TABLE IF NOT EXISTS things (id int, name text, PRIMARY KEY (id))")
    session.execute("INSERT INTO things (id, name) VALUES (2, 'bar');")
    println("things table have a new value...")


    tweetStream.foreachRDD((rdd, time) => {
      val count = rdd.count()
      if (count > 0) {
        val outputRDD = rdd.repartition(partitionsEachInterval)
        outputRDD.saveAsTextFile(outputDirectory + "/tweets_" + time.milliseconds.toString)
        println("outputRDD is: " + outputRDD)
        val aTweet = outputRDD.take(1).foreach(indvArray => indvArray.foreach(print))
        println
        println("aTweet: " + aTweet)
        session.execute("INSERT INTO things (id, name) VALUES (" + numTweetsCollected + ",'" + aTweet + "');")
        numTweetsCollected += count
        if (numTweetsCollected > numTweetsToCollect) {
          println
          println("numTweetsCollected > numTweetsToCollect condition is reached. Stopping..." + numTweetsCollected + " " + count)
          System.exit(0)
        }
      }
    })

    ssc.start()
    ssc.awaitTermination()
    println("Finished!")
  }
}
