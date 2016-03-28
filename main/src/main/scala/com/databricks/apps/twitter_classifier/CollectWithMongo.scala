package com.databricks.apps.twitter_classifier

import java.io.File

import com.google.gson.{Gson,GsonBuilder, JsonParser}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import com.mongodb.casbah.Imports._
/**
 * Collect at least the specified number of tweets into json text files.
 */
object CollectWithMongo {
  private var numTweetsCollected = 0L
  private var partNum = 0
  
  private val jsonParser = new JsonParser()
  private val gson = new GsonBuilder().setPrettyPrinting().create()

  def main(args: Array[String]) {
    // Process program arguments and set properties
    if (args.length < 3) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        "<outputDirectory> <numTweetsToCollect> <intervalInSeconds> <partitionsEachInterval>")
      System.exit(1)
    }
    val Array(outputDirectory, Utils.IntParam(numTweetsToCollect),  Utils.IntParam(intervalSecs), Utils.IntParam(partitionsEachInterval)) =Utils.parseCommandLineWithTwitterCredentials(args)
    val outputDir = new File(outputDirectory.toString)
    if (outputDir.exists()) {
      System.err.println("ERROR - %s already exists: delete or specify another directory".format(outputDirectory))
      System.exit(1)
    }
    outputDir.mkdirs()

    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[4]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth).map(gson.toJson(_))
    
    println("Initialized Streaming Spark Context.")  

    println("Initializing mongodb connector...")

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
        }
        numTweetsCollected += count
        if (numTweetsCollected > numTweetsToCollect) {
          println
          println("numTweetsCollected > numTweetsToCollect condition is reached. Stopping..." + numTweetsCollected + " " + count)
          System.exit(0)
        }
      }
    })

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
