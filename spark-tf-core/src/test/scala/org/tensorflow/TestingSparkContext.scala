/**
 *  Copyright (c) 2016 Intel Corporation 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tensorflow

import java.util.Date
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession

import scala.concurrent.Lock

/**
 * Don't use this class directly!!  Use the FlatSpec or WordSpec version for your tests
 *
 * TestingSparkSession supports two basic modes:
 *
 * 1. shared SparkSession for all tests - this is fast
 * 2. starting and stopping SparkSession for every test - this is slow but more independent
 *
 * You can't have more than one local SparkSession running at the same time.
 */
object TestingSparkSession {

  /** lock allows non-Spark tests to still run concurrently */
  private val lock = new Lock()

  /** global SparkSession that can be re-used between tests */
  private lazy val spark: SparkSession = createLocalSparkSession()

  /** System property can be used to turn off globalSparkSession easily */
  private val useGlobalSparkSession: Boolean = System.getProperty("useGlobalSparkSession", "true").toBoolean

  /**
   * Should be called from before()
   */
  def sparkSession: SparkSession = {
    if (useGlobalSparkSession) {
      // reuse the global SparkSession
      spark
    }
    else {
      // create a new SparkSession each time
      lock.acquire()
      createLocalSparkSession()
    }
  }

  /**
   * Should be called from after()
   */
  def cleanUp(): Unit = {
    if (!useGlobalSparkSession) {
      cleanupSpark()
      lock.release()
    }
  }

  private def createLocalSparkSession(
    serializer: String = "org.apache.spark.serializer.JavaSerializer",
    registrator: String = "org.apache.spark.serializer.KryoSerializer"): SparkSession = {
    System.setProperty("spark.driver.allowMultipleContexts", "true")
    turnOffLogging() // todo: add to config if needed for debug

    SparkSession
      .builder()
      .appName(this.getClass.getSimpleName + " " + new Date())
      .master("local")
      .config("spark.serializer", serializer)
      .config("spark.sql.shuffle.partitions", "2")
      .getOrCreate()
  }

  def turnOffLogging() = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
  }

  /**
   * Shutdown spark and release the lock
   */
  private def cleanupSpark(): Unit = {
    try {
      if (spark != null) {
        spark.stop()
      }
    }
    finally {
      // To avoid Akka rebinding to the same port, since it doesn't unbind immediately on shutdown
      System.clearProperty("spark.driver.port")
    }
  }

}
