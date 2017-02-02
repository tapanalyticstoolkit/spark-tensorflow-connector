package org.tensorflow

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{ DataFrame, SQLContext, SparkSession }

package object tf {

  /**
   * Adds a method, `csvFile`, to SQLContext that allows reading CSV data.
   */
  implicit class tfSession(session: SparkSession) extends Serializable {
    def tfFile(filePath: String, schema: Option[StructType] = None): DataFrame = {
      ImportTensorflow.importTensorflow(session, filePath, schema)
    }

  }

  implicit class tfFrame(dataFrame: DataFrame) {

    /**
     * Saves DataFrame as csv files. By default uses ',' as delimiter, and includes header line.
     * If compressionCodec is not null the resulting output will be compressed.
     * Note that a codec entry in the parameters map will be ignored.
     */
    def saveAsTfRecords(path: String): Unit = {
      ExportToTensorflow.exportToTensorflow(dataFrame, path)
    }
  }
}