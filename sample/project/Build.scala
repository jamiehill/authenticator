import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "security-module-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean

    //"uk.co.amelco" %% "authenticator-module" % "1.0-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    organization := "uk.co.amelco"

  );

}
