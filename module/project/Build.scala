import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "authenticator"
  val appVersion      = "1.0"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,

    "org.webjars" %% "webjars-play" % "2.1.0-2",
    "org.webjars" % "jquery" % "1.9.0",
    "org.webjars" % "bootstrap" % "3.0.0-rc1",
    "commons-io" % "commons-io" % "1.3.2",
    "com.google.code.gson" % "gson" % "2.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    organization := "uk.co.amelco",
    publishTo := Some("artifactory" at "http://amserv02:8081/artifactory/private-internal-repository"),
//    credentials += Credentials("artifactory", "http://amserv02:8081/artifactory/private-internal-repository", "amelco", "M@yTh3FBWU!")
    credentials += Credentials("artifactory", "http://amserv02:8081/artifactory/private-internal-repository", "", "")
  )

}
