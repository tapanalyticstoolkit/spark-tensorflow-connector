package org.tensorflow

import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.sql._
import org.apache.spark.sql.execution.datasources.{OutputWriterFactory, TextBasedFileFormat, HadoopFsRelation}
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType

/**
 * Provides access to tfr data from pure SQL statements (i.e. for users of the
 * JDBC server).
 */
class DefaultSource
    extends DataSourceRegister with CreatableRelationProvider {

  /**
   * Short alias for spark-tensorflow data source.
   */
  override def shortName(): String = "tf"

  def createRelation(
                      sqlContext: SQLContext,
                      mode: SaveMode,
                      parameters: Map[String, String],
                      data: DataFrame): BaseRelation = {

    val path = parameters("path")
    ExportToTensorflow.exportToTensorflow(data, path)

    new BaseRelation {
      override def sqlContext: SQLContext = sqlContext
      override def schema: StructType = data.schema
    }

  }



}