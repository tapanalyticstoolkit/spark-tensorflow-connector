package org.tensorflow

import org.apache.hadoop.io.{ BytesWritable, NullWritable }
import org.apache.spark.sql.DataFrame
import org.tensorflow.hadoop.io.TFRecordFileOutputFormat
import org.tensorflow.serde.DefaultTfRecordRowEncoder

object ExportToTensorflow {
  /**
   * Exports the current DataFrame as TensorFlow records to HDFS/Local path.
   *
   * TensorFlow records are the standard data format for TensorFlow. The recommended format for TensorFlow is a TFRecords file
   * containing tf.train.Example protocol buffers. The tf.train.Example protocol buffers encodes (which contain Features as a field).
   * https://www.tensorflow.org/how_tos/reading_data
   *
   * During export, the API parses Spark SQL DataTypes to TensorFlow compatible DataTypes as below:
   *
   * IntegerType or LongType =>  Int64List
   * FloatType or DoubleType => FloatList
   * ArrayType(Double) [Vector] => FloatList
   * Any other DataType (Ex: String) => BytesList
   *
   * @param sourceFrame DataFrame
   * @param destinationPath Full path to HDFS/Local filesystem
   */
  def exportToTensorflow(sourceFrame: DataFrame, destinationPath: String): Unit = {

    val features = sourceFrame.rdd.map(row => {
      val example = DefaultTfRecordRowEncoder.encodeTfRecord(row)
      (new BytesWritable(example.toByteArray), NullWritable.get())
    })
    features.saveAsNewAPIHadoopFile[TFRecordFileOutputFormat](destinationPath)
  }
}
