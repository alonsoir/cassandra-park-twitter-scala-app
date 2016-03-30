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

    val tweetsDF = sqlContext.read.format("com.stratio.datasource.mongodb")
                  .option("host",s"$MongoHost:$MongoPort")
                  .option("database",s"$Database")
                  .option("collection",s"$Collection")
                  .load()
                  //.table(s"$Collection")
                  //.cache()
    
    //tweetsDF.show(50)
    // Pretty print some of the tweets.
    //val tweets = sc.textFile(tweetInput)
    println("------------Sample JSON Tweets-------")
    //for (tweet <- tweets.take(5)) {
    //  println(gson.toJson(jsonParser.parse(tweet)))
    //}

    //val tweetTable = sqlContext.read.json(tweetInput).cache()
    //tweetTable.registerTempTable("tweetTable")

    println("------Tweet table Schema---")
    //tweetTable.printSchema()
    //tweetsDF.printSchema()

    println("----Sample Tweet Text-----")
    //sqlContext.sql("SELECT text FROM tweetTable LIMIT 10").collect().foreach(println)

    //sqlContext.sql("SELECT tweets FROM tweets LIMIT 10").collect().foreach(println)

    //println("------Sample Lang, Name, text---")
    //sqlContext.sql("SELECT user.lang, user.name, text FROM tweetTable LIMIT 1000").collect().foreach(println)

    //println("------Total count by languages Lang, count(*)---")
    //sqlContext.sql("SELECT user.lang, COUNT(*) as cnt FROM tweetTable GROUP BY user.lang ORDER BY cnt DESC LIMIT 25").collect.foreach(println)
    /*
    println("--- Training the model and persist it")
    val texts = sqlContext.sql("SELECT text from tweetTable").map(_.toString)
    // Cache the vectors RDD since it will be used for all the KMeans iterations.
    val vectors = texts.map(Utils.featurize).cache()
    vectors.count()  // Calls an action on the RDD to populate the vectors cache.
    val model = KMeans.train(vectors, numClusters, numIterations)
    sc.makeRDD(model.clusterCenters, numClusters).saveAsObjectFile(outputModelDir)

    val some_tweets = texts.take(100)
    println("----Example tweets from the clusters")
    for (i <- 0 until numClusters) {
      println(s"\nCLUSTER $i:")
      some_tweets.foreach { t =>
        if (model.predict(Utils.featurize(t)) == i) {
          println(t)
        }
      }
    }
    */
    closeMongoEnviroment(mongoClient)
  }
}
