name := "spark-twitter-lang-classifier-using-cassandra"

val commonSettings = Seq(
organization := "com.aironman",
version := "0.1",
scalaVersion := "2.10.4",
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
)

lazy val sparkDependencies = Seq (
"org.apache.spark" %% "spark-core" % "1.4.0" , 
"org.apache.spark" %% "spark-mllib" % "1.4.0" ,
"org.apache.spark" %% "spark-sql" % "1.4.0" ,
"org.apache.spark" %% "spark-streaming" % "1.4.0",
"org.apache.spark" %% "spark-streaming-twitter" % "1.4.0",
"com.google.code.gson" % "gson" % "2.3",
"org.twitter4j" % "twitter4j-core" % "3.0.3",
"commons-cli" % "commons-cli" % "1.2"
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
.settings(libraryDependencies ++= (testDependencies ++ cassandraDependencies ++ sparkDependencies))

//val projectMainClass = "common.utils.cassandra.CassandraMain"

val projectMainClass = "com.databricks.apps.twitter_classifier.Collect"

lazy val main = project.in(file("main"))
  .dependsOn(common)
  .settings(commonSettings:_*)
  .settings(mainClass := Some(projectMainClass)
  )	

// If you need to specify main classes manually, use packSettings and packMain
packSettings

// [Optional] Creating `hello` command that calls org.mydomain.Hello#main(Array[String]) 
packMain := Map("collect" -> "com.databricks.apps.twitter_classifier.Collect")

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"