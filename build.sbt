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

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter {x => List("paranamer-2.3.jar",
    "jersey-json-1.9.jar",
    "stax-api-1.0-2.jar",
    "jackson-jaxrs-1.9.13.jar",
    "jackson-core-asl-1.9.13.jar",
    "asm-3.1.jar",
    "hadoop-annotations-2.6.0.jar",
    "jersey-client-1.9.jar",
    "jaxb-api-2.2.2.jar",
    "javax.inject-1.jar",
    "xz-1.0.jar",
    "hadoop-auth-2.6.0.jar",
    "guice-servlet-3.0.jar",
    "gson-2.2.4.jar",
    "activation-1.1.jar",
    "jackson-xc-1.9.13.jar",
    "httpcore-4.2.4.jar",
    "aopalliance-1.0.jar",
    "curator-recipes-2.6.0.jar",
    "commons-configuration-1.6.jar",
    "avro-1.7.4.jar",
    "httpclient-4.2.5.jar",
    "commons-httpclient-3.1.jar",
    "cglib-2.2.1-v20090111.jar",
    "jasper-runtime-5.5.23.jar",
    "jersey-server-1.9.jar",
    "guice-3.0.jar",
    "apacheds-kerberos-codec-2.0.0-M15.jar",
    "jackson-mapper-asl-1.9.13.jar",
    "jaxb-impl-2.2.3-1.jar",
    "jasper-compiler-5.5.23.jar",
    "snappy-java-1.0.4.1.jar",
    "commons-collections-3.2.1.jar",
    "guava-11.0.2.jar",
    "protobuf-java-3.1.0.jar",
    "log4j-1.2.17.jar",
    "hadoop-common-2.6.0.jar",
    "commons-el-1.0.jar",
    "scala-library-2.11.8.jar",
    "jettison-1.1.jar",
    "htrace-core-3.0.4.jar",
    "jsp-api-2.1.jar",
    "commons-compress-1.4.1.jar",
    "jets3t-0.9.0.jar",
    "commons-codec-1.4.jar",
    "commons-digester-1.8.jar",
    "commons-logging-1.1.3.jar",
    "commons-io-2.4.jar",
    "jetty-6.1.26.jar",
    "hadoop-mapreduce-client-core-2.6.0.jar",
    "servlet-api-2.5.jar",
    "java-xmlbuilder-0.4.jar",
    "jersey-guice-1.9.jar",
    "commons-lang-2.6.jar",
    "slf4j-log4j12-1.7.5.jar",
    "commons-cli-1.2.jar",
    "commons-beanutils-1.7.0.jar",
    "curator-client-2.6.0.jar",
    "jsr305-1.3.9.jar",
    "apacheds-i18n-2.0.0-M15.jar",
    "api-asn1-api-1.0.0-M20.jar",
    "jetty-util-6.1.26.jar",
    "api-util-1.0.0-M20.jar",
    "commons-math3-3.1.1.jar",
    "jline-0.9.94.jar",
    "jsch-0.1.42.jar",
    "zookeeper-3.4.6.jar",
    "jersey-core-1.9.jar",
    "commons-net-3.1.jar",
    "netty-3.7.0.Final.jar",
    "commons-beanutils-core-1.8.0.jar",
    "curator-framework-2.6.0.jar",
    "slf4j-api-1.7.5.jar",
    "hadoop-yarn-common-2.6.0.jar",
    "xmlenc-0.52.jar",
    "hadoop-yarn-api-2.6.0.jar").contains(x.data.getName)}
}

