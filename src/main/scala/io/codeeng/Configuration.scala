package io.codeeng

import com.typesafe.config.ConfigFactory

object Configuration {
  private val config = ConfigFactory.load()
  object http {
    val host: String        = config.getString("http.host")
    val port: Int           = config.getInt("http.port")
    val externalUrl: String = s"http://$host:$port"
  }

  object db {
    val user: String         = config.getString("db.properties.user")
    val password: String     = config.getString("db.properties.password")
    val host: String         = config.getString("db.properties.serverName")
    val port: String         = config.getString("db.properties.portNumber")
    val databaseName: String = config.getString("db.properties.databaseName")
    val url: String          = s"jdbc:postgresql://$host:$port/$databaseName"
  }
}
