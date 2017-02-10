package org.trustedanalytics.spark.datasources.tensorflow

import org.apache.hadoop.io.{BytesWritable, NullWritable}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.tensorflow.example.Example
import org.tensorflow.hadoop.io.TFRecordFileInputFormat
import org.trustedanalytics.spark.datasources.tensorflow.serde.DefaultTfRecordRowDecoder


case class TensorflowRelation(options: Map[String, String])(@transient val session: SparkSession) extends BaseRelation with TableScan {

  //Import TFRecords as DataFrame happens here
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
