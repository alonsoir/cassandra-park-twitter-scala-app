# cassandra-spark-twitter-scala-app

The purpose of this tiny project is to learn about how to interact with a Cassandra db and mongo db using scala, in order to use them with Apache Spark and Twitter.

The idea is to integrate the databricks reference app, which uses spark-streaming and machine learning techniques with the necessary code from Manuel Kiessling to interact with a cassandra instance. I am going to use casbah to interact with a local mongodb instance.

The project is packed with sbt-pack, so in order to get running this project, download it to a local folder and run sbt and inside it, pack, that is all, sbt-pack will compile and pack in a uber jar all the necessary and it will create a unix command to run the code:

	MacBook-Pro-Retina-de-Alonso:~ aironman$ cd Downloads/
	MacBook-Pro-Retina-de-Alonso:Downloads aironman$ cd cassandra-spark-twitter-scala-app-master/
	MacBook-Pro-Retina-de-Alonso:cassandra-spark-twitter-scala-app-master aironman$ sbt
	[info] Loading project definition from /Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/project
	[info] Updating {file:/Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/project/}cassandra-spark-twitter-scala-app-master-build...
	[info] Resolving org.fusesource.jansi#jansi;1.4 ...
	[info] Done updating.
	[info] Set current project to spark-twitter-lang-classifier-using-cassandra (in build file:/Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/)
	> pack
	[info] Updating {file:/Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/}cassandra-spark-twitter-scala-app-master...
	[info] Updating {file:/Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/}common...
	[info] Resolving org.fusesource.jansi#jansi;1.4 ...
	[info] Done updating.
	[info] Resolving com.codahale.metrics#metrics-core;3.0.2 ...
	[info] Packaging /Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/target/scala-2.10/spark-twitter-lang-classifier-using-cassandra_2.10-0.1-SNAPSHOT.jar ...
	[info] Resolving com.chrisomeara#pillar_2.10;2.0.1 ...
	[info] Done packaging.
	[info] Resolving org.fusesource.jansi#jansi;1.4 ...
	[info] Done updating.
	[info] Updating {file:/Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/}main...
	[info] Resolving com.google.guava#guava;16.0.1 ...
	[info] Compiling 3 Scala sources and 1 Java source to /Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/common/target/scala-2.10/classes...
	[info] Resolving org.fusesource.jansi#jansi;1.4 ...
	[info] Done updating.
	[info] Packaging /Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/common/target/scala-2.10/common_2.10-0.1.jar ...
	[info] Compiling 5 Scala sources to /Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/main/target/scala-2.10/classes...
	[info] Done packaging.
	[warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list
	[info] Packaging /Users/aironman/Downloads/cassandra-spark-twitter-scala-app-master/main/target/scala-2.10/main_2.10-0.1.jar ...
	[info] Done packaging.
	[info] Creating a distributable package in target/pack
	[info] Copying libraries to target/pack/lib
	blablabla...
	[info] Generating target/pack/VERSION
	[info] done.
	[success] Total time: 10 s, completed 16-mar-2016 9:58:35
	> exit
	MacBook-Pro-Retina-de-Alonso:cassandra-spark-twitter-scala-app-master aironman$ target/pack/bin/collect
	Usage: Collect$<outputDirectory> <numTweetsToCollect> <intervalInSeconds> <partitionsEachInterval>
	MacBook-Pro-Retina-de-Alonso:cassandra-spark-twitter-scala-app-master aironman$ 

You can see that this new command needs four parameters, this is an output in my local machine: 


	MacBook-Pro-Retina-de-Alonso:my-twitter-cassandra-app aironman$ target/pack/bin/collect /tmp/tweets 50 10 1
	Initializing Streaming Spark Context...
	Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
	16/03/16 10:23:48 INFO SparkContext: Running Spark version 1.4.0
	16/03/16 10:23:48 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
	16/03/16 10:23:49 INFO SecurityManager: Changing view acls to: aironman
	16/03/16 10:23:49 INFO SecurityManager: Changing modify acls to: aironman
	16/03/16 10:23:49 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Set(aironman); users with modify permissions: Set(aironman)
	16/03/16 10:23:49 INFO Slf4jLogger: Slf4jLogger started
	16/03/16 10:23:49 INFO Remoting: Starting remoting
	16/03/16 10:23:49 INFO Remoting: Remoting started; listening on addresses :[akka.tcp://sparkDriver@192.168.1.34:49324]
	16/03/16 10:23:49 INFO Utils: Successfully started service 'sparkDriver' on port 49324.
	16/03/16 10:23:49 INFO SparkEnv: Registering MapOutputTracker
	16/03/16 10:23:49 INFO SparkEnv: Registering BlockManagerMaster
	16/03/16 10:23:49 INFO DiskBlockManager: Created local directory at /private/var/folders/gn/pzkybyfd2g5bpyh47q0pp5nc0000gn/T/spark-1ed28d7f-a50b-422d-ab1d-8bcc55d2c6e1/blockmgr-220eb8ea-aba8-4054-baac-166f7ba921d3
	16/03/16 10:23:49 INFO MemoryStore: MemoryStore started with capacity 1966.1 MB
	16/03/16 10:23:49 INFO HttpFileServer: HTTP File server directory is /private/var/folders/gn/pzkybyfd2g5bpyh47q0pp5nc0000gn/T/spark-1ed28d7f-a50b-422d-ab1d-8bcc55d2c6e1/httpd-fca8296a-7c5e-4dea-9182-3925dd3c3dd9
	16/03/16 10:23:49 INFO HttpServer: Starting HTTP Server
	16/03/16 10:23:50 INFO Utils: Successfully started service 'HTTP file server' on port 49325.
	16/03/16 10:23:50 INFO SparkEnv: Registering OutputCommitCoordinator
	16/03/16 10:23:50 INFO Utils: Successfully started service 'SparkUI' on port 4040.
	16/03/16 10:23:50 INFO SparkUI: Started SparkUI at http://192.168.1.34:4040
	16/03/16 10:23:50 INFO Executor: Starting executor ID driver on host localhost
	16/03/16 10:23:50 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 49326.
	16/03/16 10:23:50 INFO NettyBlockTransferService: Server created on 49326
	16/03/16 10:23:50 INFO BlockManagerMaster: Trying to register BlockManager
	16/03/16 10:23:50 INFO BlockManagerMasterEndpoint: Registering block manager localhost:49326 with 1966.1 MB RAM, BlockManagerId(driver, localhost, 49326)
	16/03/16 10:23:50 INFO BlockManagerMaster: Registered BlockManager
	Initialized Streaming Spark Context.
	Initializing Cassandra...
	host is: localhost
	port is: 9042
	keyspace is: test
	16/03/16 10:23:51 INFO DCAwareRoundRobinPolicy: Using data-center name 'datacenter1' for DCAwareRoundRobinPolicy (if this is incorrect, please provide the correct datacenter name with DCAwareRoundRobinPolicy constructor)
	16/03/16 10:23:51 INFO Cluster: New Cassandra host localhost/127.0.0.1:9042 added
	You have a open Cassandra session...
	things table have a new value...
	16/03/16 10:23:51 INFO ReceiverTracker: ReceiverTracker start
	....


Actually the code is saving one entry within a keyspace named test and within this keyspace there is a table named things, i am saving into it a pair with the values (2,bar), as you can see in this output:

	Last login: Wed Mar 16 09:35:15 on ttys000
	MacBook-Pro-Retina-de-Alonso:~ aironman$ dsc-cassandra-2.1.9/bin/cqlsh
	Connected to Test Cluster at 127.0.0.1:9042.
	[cqlsh 5.0.1 | Cassandra 2.1.9 | CQL spec 3.2.0 | Native protocol v3]
	Use HELP for help.
	cqlsh> use test;
	cqlsh:test> select * from things;

	 id | name
	----+------
	  1 |  foo
	  2 |  bar

	(2 rows)
	cqlsh:test>

In order to create this keyspace and this table, open cqlsh and run the next commands:

	CREATE KEYSPACE IF NOT EXISTS test WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

	use test; // you have to change to this keyspace!

	CREATE TABLE things (
	  id int,
	  name text,
	  PRIMARY KEY (id)
	);

TODO 

	1) I have to learn how to parse the json from twitter using if possible the scala-lang library and save 
	it within the cassandra instance.

	2) save the tweet json into a mongodb instance, it looks more appropiate to store the full json 
	into mongo...

	3) update the others main objects provided by the reference-app, ExamineAndTrain.scala and Train.scala 
	to read from Cassandra and Mongo instances and compare the results.

	4) get fun in the process!

next step

Use and adapt this to store data in mongo instance or use another library to save josn tweets into a local Mongo instance, ie, spark-mongodb from Stratio. 

	dstream.foreachRDD { rdd =>
	  rdd.foreachPartition { partitionOfRecords =>
	    // ConnectionPool is a static, lazily initialized pool of connections
	    val connection = ConnectionPool.getConnection()
	    partitionOfRecords.foreach(record => connection.send(record))
	    ConnectionPool.returnConnection(connection)  // return to the pool for future reuse
	  }
	}

or 

	tweets.foreachRDD { x => 
	 x.foreach { x => 
	  val db = connector("localhost", "rmongo", "rmongo", "pass")
	  val dbcrud = new DBCrud(db, "table1")
	  dbcrud.insert(x) 
	 } 
	}

or
	
	dstream.foreachRDD { rdd =>
	  rdd.foreachPartition { partitionOfRecords =>
	    // ConnectionPool is a static, lazily initialized pool of connections
	    val connection = ConnectionPool.getConnection()
	    partitionOfRecords.foreach(record => connection.send(record))
	    ConnectionPool.returnConnection(connection)  // return to the pool for future reuse
	  }
	}


http://spark.apache.org/docs/latest/streaming-programming-guide.html#design-patterns-for-using-foreachrdd

https://github.com/Stratio/Spark-MongoDB/blob/master/spark-mongodb-examples/src/main/scala/com/stratio/datasource/mongodb/examples/DataFrameAPIExample.scala

