package org.tensorflow

import org.apache.hadoop.io.{ BytesWritable, NullWritable }
import org.apache.spark.sql.DataFrame
import org.tensorflow.hadoop.io.TFRecordFileOutputFormat
import org.tensorflow.serde.DefaultTfRecordRowEncoder

object ExportToTensorflow {

  def exportToTensorflow(sourceFrame: DataFrame, destinationPath: String): Unit = {

    val features = sourceFrame.rdd.map(row => {
      val example = DefaultTfRecordRowEncoder.encodeTfRecord(row)
      (new BytesWritable(example.toByteArray), NullWritable.get())
    })
    features.saveAsNewAPIHadoopFile[TFRecordFileOutputFormat](destinationPath)
  }
}
