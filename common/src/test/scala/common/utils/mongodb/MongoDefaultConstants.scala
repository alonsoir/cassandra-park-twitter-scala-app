package common.utils.mongodb

/**
this wired config must be a json...
*/
trait MongoDefaultConstants {
  val Database = "highschool"
  val Collection = "students"
  val MongoHost = "127.0.0.1"
  val MongoPort = 27017
  val MongoProvider = "com.stratio.datasource.mongodb"
}

