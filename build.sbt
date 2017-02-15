scalaVersion in Global := "2.11.8"

def ProjectName(name: String,path:String): Project =  Project(name, file(path))

resolvers in Global ++= Seq("https://tap.jfrog.io/tap/public" at "https://tap.jfrog.io/tap/public" ,
           "https://tap.jfrog.io/tap/public-snapshots" at "https://tap.jfrog.io/tap/public-snapshots" ,
           "https://repo.maven.apache.org/maven2" at "https://repo.maven.apache.org/maven2" )

val `junit_junit` = "junit" % "junit" % "4.12"

val `org.apache.hadoop_hadoop-yarn-api` = "org.apache.hadoop" % "hadoop-yarn-api" % "2.7.3"

val `org.apache.spark_spark-core_2.11` = "org.apache.spark" % "spark-core_2.11" % "2.1.0"

val `org.apache.spark_spark-sql_2.11` = "org.apache.spark" % "spark-sql_2.11" % "2.1.0"

val `org.apache.spark_spark-mllib_2.11` = "org.apache.spark" % "spark-mllib_2.11" % "2.1.0"

val `org.scalatest_scalatest_2.11` = "org.scalatest" % "scalatest_2.11" % "2.2.6"

val `org.tensorflow_tensorflow-hadoop` = "org.tensorflow" % "tensorflow-hadoop" % "1.0-01232017-SNAPSHOT"

version := "1.0-SNAPSHOT"

name := "spark-tensorflow-connector"

organization := "org.trustedanalytics"

libraryDependencies in Global ++= Seq(`org.tensorflow_tensorflow-hadoop` classifier "shaded-protobuf",
   `org.scalatest_scalatest_2.11` % "test" ,
   `org.apache.spark_spark-sql_2.11` % "provided" ,
   `org.apache.spark_spark-mllib_2.11` % "test" classifier "tests" ,
   `org.apache.spark_spark-core_2.11` % "provided" ,
   `org.apache.hadoop_hadoop-yarn-api` % "provided" ,
   `junit_junit` % "test" )

assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}