name := "spark-tensorflow-connector"

organization := "org.trustedanalytics"

spName := "trustedanalytics/spark-tensorflow-connector"

sparkVersion := "2.1.0"

sparkComponents := Seq("sql")

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.0", "2.11.8")


/********************
  * Release settings *
  ********************/

publishMavenStyle := true

releaseCrossBuild := true

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

releasePublishArtifactsAction := PgpKeys.publishSigned.value

pomExtra :=
  <url>https://github.com/tapanalyticstoolkit/spark-tensorflow-connector</url>
    <scm>
      <url>git@github.com:tapanalyticstoolkit/spark-tensorflow-connector.git</url>
      <connection>scm:git:git@github.com:tapanalyticstoolkit/spark-tensorflow-connector.git</connection>
    </scm>
    <developers>
      <developer>
        <id>karthikvadla16</id>
        <name>Karthik Vadla</name>
        <url>https://github.com/karthikvadla16</url>
      </developer>
      <developer>
        <id>skavulya</id>
        <name>Soila Kavulya</name>
        <url>https://github.com/skavulya</url>
      </developer>
      <developer>
        <id>joyeshmishra</id>
        <name>Joyesh Mishra</name>
        <url>https://github.com/joyeshmishra</url>
      </developer>
    </developers>

bintrayReleaseOnPublish in ThisBuild := false

import ReleaseTransformations._

// Add publishing to spark packages as another step.
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges,
  releaseStepTask(spPublish)
)