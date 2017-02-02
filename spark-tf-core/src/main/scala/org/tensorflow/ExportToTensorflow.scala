package org.tensorflow

import org.apache.hadoop.io.{ BytesWritable, NullWritable }
import org.apache.spark.sql.DataFrame
import org.tensorflow.hadoop.io.TFRecordFileOutputFormat
import org.tensorflow.serde.DefaultTfRecordRowEncoder

object ExportToTensorflow {

  def exportToTensorflow(sourceFrame: DataFrame, destinationPath: String): Unit = {

    val spark = sourceFrame.sparkSession
    import spark.implicits._

    val features = sourceFrame.map(row => {
      val example = DefaultTfRecordRowEncoder.encodeTfRecord(row)
      (new BytesWritable(example.toByteArray), NullWritable.get())
    })
    println(s"DestinationPath: $destinationPath")
    features.rdd.saveAsNewAPIHadoopFile[TFRecordFileOutputFormat](destinationPath)
  }
}
