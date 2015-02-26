import SonatypeKeys._

sonatypeSettings

name := "social-api-scala"

organization  := "com.github.kfang"

scalaVersion  := "2.11.5"

crossScalaVersions := Seq("2.10.4", "2.11.5")

version := "0.6.0-SNAPSHOT"

scalacOptions ++= Seq("-feature", "-deprecation")

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq(
  "org.scalaj"              %% "scalaj-http"            % "1.1.4",
  "io.spray"                %% "spray-json"             % "1.3.1"
)

pomExtra := {
  <url>https://github.com/kfang/social-api-scala</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>http://opensource.org/licenses/MIT</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:git@github.com:kfang/social-api-scala.git</connection>
      <developerConnection>scm:git:git@github.com:kfang/social-api-scala.git</developerConnection>
      <url>git@github.com:kfang/social-api-scala.git</url>
    </scm>
    <developers>
      <developer>
        <id>kfang</id>
        <name>kfang</name>
        <url>https://github.com/kfang</url>
      </developer>
    </developers>
}

