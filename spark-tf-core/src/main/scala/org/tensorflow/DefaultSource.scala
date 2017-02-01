package org.tensorflow

import org.apache.spark.sql.sources._

/**
 * Provides access to CSV data from pure SQL statements (i.e. for users of the
 * JDBC server).
 */
class DefaultSource
    extends DataSourceRegister {

  /**
   * Short alias for spark-tensorflow data source.
   */
  override def shortName(): String = "tf"

}