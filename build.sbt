name := "tcrawler"

 organization := "com.tvc.bigdata"

 version := "1.0.0"

 scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "commons-httpclient" % "commons-httpclient" % "3.1",
  "org.slf4j" % "slf4j-log4j12" % "1.7.21",
  "com.tvc.be" % "dbwrapper" % "1.0.0",
  "net.sourceforge.nekohtml" % "nekohtml" % "1.9.15"
 )

javacOptions ++= Seq("-encoding", "UTF-8")

resolvers ++= Seq(
     "tvc Repository" at "http://nexus.mindcenter.cn:12580/nexus/content/groups/public/"
)


EclipseKeys.withSource := true

publishMavenStyle := true



publishTo <<= version { v: String =>
  val nexus = "http://nexus.mindcenter.cn:12580/nexus" 
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "/content/repositories/snapshots/")
  else
    Some("releases" at nexus + "/content/repositories/releases/")
}