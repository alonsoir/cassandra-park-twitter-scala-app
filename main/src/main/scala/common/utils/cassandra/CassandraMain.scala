package common.utils.cassandra

import common.utils.cassandra._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder._


object CassandraMain extends App{
	println("Trying to connect to Cassandra server...")
    val uri = CassandraConnectionUri("cassandra://localhost:9042/test")
    val session = Helper.createSessionAndInitKeyspace(uri)
	println("Connected to Cassandra server...")
	val selectStmt = select().column("name")
        .from("things")
        .where(QueryBuilder.eq("id", 1))
        .limit(1)
      
    val resultSet = session.execute(selectStmt)
    val row = resultSet.one()
    println("row is " + row.getString("name"))
    //row.getString("name") should be("foo")
	println("Finishing connection to Cassandra")
	session.close   
	println("Finished!")
}
