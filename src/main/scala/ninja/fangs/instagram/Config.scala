package ninja.fangs.instagram

import com.typesafe.config.ConfigFactory
import scalaj.http.HttpOptions

object Config {

  private val CONFIG = ConfigFactory.load()
  val CLIENT_ID       = CONFIG.getString("instagram-client.id")
  val CLIENT_SECRET   = CONFIG.getString("instagram-client.secret")
  val REDIRECT_URI    = CONFIG.getString("instagram-client.redirect-uri")
  val HTTP_OPTS       = List(
    HttpOptions.readTimeout(CONFIG.getInt("instagram-client.read-timeout")),
    HttpOptions.connTimeout(CONFIG.getInt("instagram-client.connect-timeout"))
  )

  val AUTH_EXPLICIT_URL = s"https://api.instagram.com/oauth/authorize/?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&response_type=code"
  val AUTH_IMPLICIT_URL = s"https://instagram.com/oauth/authorize/?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&response_type=token"

  val REQUEST_ACCESS_TOKEN = "https://api.instagram.com/oauth/access_token"

}
