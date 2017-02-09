package org.tensorflow.tf

import org.apache.spark.sql.SparkSession
import org.scalatest.{Suite, BeforeAndAfterAll}

trait SharedSparkSessionSuite extends BeforeAndAfterAll  { this: Suite =>

@transient protected var sparkSession: SparkSession = null
  
  override def beforeAll() = {
    sparkSession = SparkSession.builder()
      .master("local[2]")
      .appName(getClass().getSimpleName())
      .getOrCreate()
  }

  override def afterAll() = {
    sparkSession.stop()
    sparkSession = null
  }
}