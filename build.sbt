name := "npj"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.3.0" % Test
)

scalacOptions += "-deprecation"

testOptions in Test += Tests.Argument("-oI")