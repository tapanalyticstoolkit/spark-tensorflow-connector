package org.tensorflow

import org.apache.hadoop.io.{NullWritable, BytesWritable}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType
import org.tensorflow.example.Example
import org.tensorflow.hadoop.io.TFRecordFileInputFormat
import org.tensorflow.serde.DefaultTfRecordRowDecoder

/**
 * Provides access to tensorflow record source
 */
class DefaultSource
    extends DataSourceRegister with CreatableRelationProvider with RelationProvider {

  /**
   * Short alias for spark-tensorflow data source.
   */
  override def shortName(): String = "tf"

  // write path
  def createRelation(
                      sqlContext: SQLContext,
                      mode: SaveMode,
                      parameters: Map[String, String],
                      data: DataFrame): BaseRelation = {

    val path = parameters("path")
    ExportToTensorflow.exportToTensorflow(data, path)
    TfRelation(parameters)(sqlContext.sparkSession)
  }

  // read path
  def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): TfRelation = {
    TfRelation(parameters)(sqlContext.sparkSession)
  }


}

case class TfRelation(options: Map[String, String])(@transient val session: SparkSession) extends BaseRelation with TableScan {

  lazy val (tf_rdd, tf_schema) = {
    val rdd = session.sparkContext.newAPIHadoopFile(options("path"), classOf[TFRecordFileInputFormat], classOf[BytesWritable], classOf[NullWritable])

    val exampleRdd = rdd.map {
      case (bytesWritable, nullWritable) => Example.parseFrom(bytesWritable.getBytes)
    }

    val finalSchema = TensorflowInferSchema(exampleRdd)

    (exampleRdd.map(example => DefaultTfRecordRowDecoder.decodeTfRecord(example, finalSchema)), finalSchema)
  }

  override def sqlContext: SQLContext = session.sqlContext

  override def schema: StructType = tf_schema

  override def buildScan(): RDD[Row] = tf_rdd
}
