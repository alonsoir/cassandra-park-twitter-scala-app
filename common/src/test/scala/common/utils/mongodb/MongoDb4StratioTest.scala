package common.utils.mongodb

import org.scalatest.{Matchers, FunSpec}
import com.mongodb.QueryBuilder
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
//import com.stratio.datasource.mongodb.examples.DataFrameAPIExample._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}

class MongoDb4StratioTest extends FunSpec with Matchers with MongoDefaultConstants{
	
	//DeregisterConversionHelpers()
	//RegisterJodaTimeConversionHelpers()
	def withSQLContext(block: SQLContext => Unit) = {

	    val sparkConf = new SparkConf().
	      setAppName("MongoDb4StratioTest").
	      setMaster("local[4]")

	    val sc = new SparkContext(sparkConf)
	    try {
	      val sqlContext = new SQLContext(sc)
	      block(sqlContext)
	    } finally {
	      sc.stop()
	    }

  	}

  	def prepareEnvironment(): MongoClient = {
	    val mongoClient = MongoClient(MongoHost, MongoPort)
	    populateTable(mongoClient)
	    mongoClient
  	}

  	def cleanEnvironment(mongoClient: MongoClient) = {
    	cleanData(mongoClient)
    	mongoClient.close()
  	}

  	private def populateTable(client: MongoClient): Unit = {

	    val collection = client(Database)(Collection)
	    for (a <- 1 to 10) {
	      collection.insert {
	        MongoDBObject("id" -> a.toString,
	          "age" -> (10 + a),
	          "description" -> s"description $a",
	          "enrolled" -> (a % 2 == 0),
	          "name" -> s"Name $a"
	        )
	      }
	    }

	    collection.update(QueryBuilder.start("age").greaterThan(14).get, MongoDBObject(("$set", MongoDBObject(("optionalField", true)))), multi = true)
	    collection.update(QueryBuilder.start("age").is(14).get, MongoDBObject(("$set", MongoDBObject(("fieldWithSubDoc", MongoDBObject(("subDoc", MongoDBList("foo", "bar"))))))))
  	}

  	private def cleanData(client: MongoClient): Unit = {
    	val collection = client(Database)(Collection)
    	collection.dropCollection()
  	}


	describe("testing a mongodb connection with stratio library...") {
    it("should just work") {
        println("testing a mongodb connection with stratio library...")

        val mongoClient = prepareEnvironment()
        
  		withSQLContext { sqlContext =>

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


    		val studentsDF = sqlContext.read.format("com.stratio.datasource.mongodb").table("students")
    		studentsDF.where(studentsDF("age") > 15).groupBy(studentsDF("enrolled")).agg(avg("age"), max("age")).show(5)
    		println("tested a mongodb connection with stratio library...")
		}//withSQLContext
		cleanEnvironment(mongoClient)
		println("mongo enviroment is cleaned!")
    }//it("should just work")
  }//describe("testing a mongodb connection")

}


