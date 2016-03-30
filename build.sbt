name := "spark-twitter-lang-classifier-using-cassandra"

val commonSettings = Seq(
organization := "com.aironman",
version := "0.1",
scalaVersion := "2.10.6",
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
)

//lazy val nscala_time = Seq (
//"com.github.nscala-time" %% "nscala-time" % "2.12.0"
//)

lazy val mongoDependencies = Seq (
"com.stratio.datasource" % "spark-mongodb_2.10" % "0.11.1"
)

//this libraries are included within spark!
//lazy val jsonDependencies = Seq (
//"org.json4s" % "json4s-native_2.10" % "3.3.0" % "provided",
//"org.json4s" % "json4s-jackson_2.10" % "3.3.0" % "provided"
//)

lazy val sparkDependencies = Seq (
"org.apache.spark" % "spark-core_2.10" % "1.6.1" exclude("com.fasterxml.jackson.core", "jackson-databind"), 
"org.apache.spark" % "spark-mllib_2.10" % "1.6.1" ,
"org.apache.spark" % "spark-sql_2.10" % "1.6.1" ,
"org.apache.spark" % "spark-streaming_2.10" % "1.6.1",
"org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.1",
"com.google.code.gson" % "gson" % "2.6.2",
"org.twitter4j" % "twitter4j-core" % "4.0.4",
"commons-cli" % "commons-cli" % "1.3.1"
)

lazy val testDependencies = Seq (
"org.scalatest" %% "scalatest" % "2.2.0" % "test"
)

lazy val cassandraDependencies = Seq (
"com.datastax.cassandra" % "cassandra-driver-core" % "2.1.2",
"com.chrisomeara" % "pillar_2.10" % "2.0.1"
)

lazy val common = project.in(file("common"))
.settings(commonSettings:_*)
.settings(libraryDependencies ++= (testDependencies ++ cassandraDependencies ++ sparkDependencies ++ mongoDependencies))

val projectMainClass = "com.databricks.apps.twitter_classifier.CollectWithMongo"

lazy val main = project.in(file("main"))
  .dependsOn(common)
  .settings(commonSettings:_*)
  .settings(mainClass := Some(projectMainClass)
)	

packAutoSettings

// If you need to specify main classes manually, use packSettings and packMain
//packSettings

// [Optional] Creating `hello` command that calls org.mydomain.Hello#main(Array[String]) 
//packMain := Map("collectWithMongo" -> "com.databricks.apps.twitter_classifier.CollectWithMongo")

//packMain := Map("examineAndTrainWithMongo" -> "com.databricks.apps.twitter_classifier.ExamineAndTrainWithMongo")

//packMain := Map("test-cassandra" -> "common.utils.cassandra.CassandraMain")

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"


