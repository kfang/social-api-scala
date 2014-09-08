name := "scala-instagram"

organization  := "ninja.fangs"

scalaVersion  := "2.10.3"

scalacOptions ++= Seq("-feature", "-deprecation")


resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq(
  "org.scalaj"              %% "scalaj-http"            % "0.3.14",
  "com.typesafe"            %  "config"                 % "1.2.1",
  "io.spray"                %% "spray-json"             % "1.2.6"
)

fork := true


