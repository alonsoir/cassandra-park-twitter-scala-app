package common.utils.mongodb

import com.mongodb.casbah.Imports._
import org.scalatest.{Matchers, FunSpec}

//http://api.mongodb.org/scala/casbah/2.0/tutorial.html


class MongoDb4Test extends FunSpec with Matchers{
	
	//DeregisterConversionHelpers()
	//RegisterJodaTimeConversionHelpers()


	describe("testing a mongodb connection") {
    it("should just work") {
        println("testing mongodb connection...")

        val mongoClient = MongoClient("localhost", 27017)
        val db = mongoClient("test")
		db.collectionNames
        val coll = db("test")
        //some crud operations...
        val a = MongoDBObject("hello" -> "world")
		val b = MongoDBObject("language" -> "scala")
		coll.insert( a )
		coll.insert( b )
		coll.count()
		val allDocs = coll.find()
		println( allDocs )
		for(doc <- allDocs) println( doc )
		val hello = MongoDBObject("hello" -> "world")
		val helloWorld = coll.findOne( hello )

		// Find a document that doesn't exist
		val goodbye = MongoDBObject("goodbye" -> "world")
		val goodbyeWorld = coll.findOne( goodbye )

		val query = MongoDBObject("language" -> "scala")
		val update = MongoDBObject("platform" -> "JVM")
		val result = coll.update( query, update )

		println("Number updated: " + result.getN)
		for (c <- coll.find) println(c)

		//update
		val queryUpdate = MongoDBObject("platform" -> "JVM")
		val updateUpdate = $set("language" -> "Scala")
		val resultUpdate = coll.update( queryUpdate, updateUpdate )

		println( "Number updated: " + resultUpdate.getN )
		for ( c <- coll.find ) println( c )

		//upserts
		val queryUpserts = MongoDBObject("language" -> "clojure")
		val updateUpserts = $set("platform" -> "JVM")
		val resultUpserts = coll.update( queryUpserts, updateUpserts, upsert=true )

		println( "Number updated: " + resultUpserts.getN )
		for (c <- coll.find) println(c)

		//removing a document
		val queryRemovingDocument = MongoDBObject("language" -> "clojure")
		val resultRemovingDocument = coll.remove( queryRemovingDocument )
		println("Number removed: " + resultRemovingDocument.getN)
		for (c <- coll.find) println(c)

		//removing all documents
		val queryRemovingAllDocuments = MongoDBObject()
		val resultRemovingAllDocuments = coll.remove( queryRemovingAllDocuments )
		println( "Number removed: " + resultRemovingAllDocuments.getN )
		println( coll.count() )

		//dropping the collection
		coll.drop()
    }//it("should just work")
  }//describe("testing a mongodb connection")

}


