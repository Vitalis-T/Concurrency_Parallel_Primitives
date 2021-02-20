name := "concurrency-examples"

version := "1.0"

scalaVersion := "2.13.0"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "com.storm-enroute" %% "scalameter" % "0.19" % Test
  )

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

parallelExecution in Test := false

fork := false

outputStrategy := Some(StdoutOutput)

connectInput := true



