package org.tensorflow

import org.apache.hadoop.io.{ BytesWritable, NullWritable }
import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.tensorflow.hadoop.io.TFRecordFileOutputFormat
import org.tensorflow.serde.DefaultTfRecordRowEncoder

/**
 * Provides access to TensorFlow record source
 */
class DefaultSource
    extends DataSourceRegister with CreatableRelationProvider with RelationProvider {

  /**
   * Short alias for spark-tensorflow data source.
   */
  override def shortName(): String = "tf"

  // Writes DataFrame as TensorFlow Records
  override def createRelation(
    sqlContext: SQLContext,
    mode: SaveMode,
    parameters: Map[String, String],
    data: DataFrame): BaseRelation = {

    val path = parameters("path")

    //Export DataFrame as TFRecords
    val features = data.rdd.map(row => {
      val example = DefaultTfRecordRowEncoder.encodeTfRecord(row)
      (new BytesWritable(example.toByteArray), NullWritable.get())
    })
    features.saveAsNewAPIHadoopFile[TFRecordFileOutputFormat](path)

    TensorflowRelation(parameters)(sqlContext.sparkSession)
  }

  // Reads TensorFlow Records into DataFrame
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): TensorflowRelation = {
    TensorflowRelation(parameters)(sqlContext.sparkSession)
  }
}