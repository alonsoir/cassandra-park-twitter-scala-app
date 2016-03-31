package com.databricks.apps.twitter_classifier

import com.google.gson.{GsonBuilder, JsonParser}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

import com.mongodb.casbah.Imports._
import com.mongodb.QueryBuilder
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import org.apache.spark.sql.functions._

/**
 * Examine the collected tweets saved within a mongo instance and trains a model based on them.
 */
object ExamineAndTrainWithMongo {

  private val Database = "alonsodb"
  private val Collection = "tweets"
  private val MongoHost = "127.0.0.1"
  private val MongoPort = 27017
  private val MongoProvider = "com.stratio.datasource.mongodb"

  private val jsonParser = new JsonParser()
  private val gson = new GsonBuilder().setPrettyPrinting().create()

  private def prepareMongoEnvironment(): MongoClient = {
      val mongoClient = MongoClient(MongoHost, MongoPort)
      mongoClient
  }

  private def closeMongoEnviroment(mongoClient : MongoClient) = {
      println
      mongoClient.close()
      println("mongoclient closed!")
  }

  private def cleanMongoEnvironment(mongoClient: MongoClient) = {
      cleanMongoData(mongoClient)
      mongoClient.close()
  }

  private def cleanMongoData(client: MongoClient): Unit = {
      val collection = client(Database)(Collection)
      collection.dropCollection()
  }

  def main(args: Array[String]) {
    // Process program arguments and set properties
    if (args.length < 2) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        " <outputModelDir> <numClusters> <numIterations>")
      System.exit(1)
    }
    val Array(outputModelDir, Utils.IntParam(numClusters), Utils.IntParam(numIterations)) = args
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[4]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    println("Initializing mongodb connector...")

    val mongoClient = prepareMongoEnvironment()
    val collection = mongoClient(Database)(Collection)
    
    println("Initialized mongodb connector...")
    //loading dataframe
    val tweetsDF = sqlContext.read.format("com.stratio.datasource.mongodb")
                  .option("host",s"$MongoHost:$MongoPort")
                  .option("database",s"$Database")
                  .option("collection",s"$Collection")
                  .load()
    
    //tweetsDF.show(5)
    
    println("tweets saved in mongo instance: " + tweetsDF.count())
    println("actual schema is: ")
    tweetsDF.printSchema()
    println
    //tweetsDF.select("tweets").show(500)

    // Register the DataFrames as a table. important! you must do it in order to work with stored data
    tweetsDF.registerTempTable("mytweets")

    // SQL statements can be run by using the sql methods provided by sqlContext.
    // The results of SQL queries are DataFrames and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by field index or by field name.

    //results.map(t => "id: " + t(0)).collect().foreach(println)
    //results.map(t => "tweets: " + t(1)).collect().foreach(println)

    println("------Total count by id and tweets, count(*)---")
    val sometweets = sqlContext.sql("SELECT id, tweets, COUNT(*) as cnt FROM mytweets GROUP BY id,tweets ORDER BY cnt DESC LIMIT 25").collect()

    sometweets.foreach(println)

    println("--- Training the model and persist it")
    
    val texts = sqlContext.sql("SELECT tweets from mytweets").map(_.toString)

    //texts is: MapPartitionsRDD[15] at map at ExamineAndTrainWithMongo.scala:95
    println("texts is: " + texts)
    //val texts = sqlContext.sql("SELECT tweets from mytweets").map(jsonParser.parse(_.toString))
    // Cache the vectors RDD since it will be used for all the KMeans iterations.
    val vectors = texts.map(Utils.featurize).cache()
    vectors.count()  // Calls an action on the RDD to populate the vectors cache.
    val model = KMeans.train(vectors, numClusters, numIterations)
    sc.makeRDD(model.clusterCenters, numClusters).saveAsObjectFile(outputModelDir)

    val some_tweets = texts.take(10000)
    println("----Example tweets from the clusters")
    for (i <- 0 until numClusters) {
      println(s"\nCLUSTER $i:")
      some_tweets.foreach { t =>
                          if (model.predict(Utils.featurize(t)) == i) {
                            //println("t is: " + t)
                            print(".")
                          }
                          }//some_tweets.foreach
    }//for (i <- 0 until numClusters)
    
    closeMongoEnviroment(mongoClient)
    println("Closed mongodb connector...")
    System.exit(0)
}//main
}//object ExamineAndTrainWithMongo
