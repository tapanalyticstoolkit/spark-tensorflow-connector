package org.tensorflow

import org.apache.hadoop.io.{ BytesWritable, NullWritable }
import org.apache.spark.sql.DataFrame
import org.tensorflow.hadoop.io.TFRecordFileOutputFormat
import org.tensorflow.serde.DefaultTfRecordRowEncoder

object ExportToTensorflow {

  def exportToTensorflow(sourceFrame: DataFrame, destinationPath: String): Unit = {
    val features = sourceFrame.map(row => {
      val example = DefaultTfRecordRowEncoder.encodeTfRecord(row)
      (new BytesWritable(example.toByteArray), NullWritable.get())
    })
    println(s"DestinationPath: $destinationPath")
    features.saveAsNewAPIHadoopFile[TFRecordFileOutputFormat](destinationPath)
  }

}
