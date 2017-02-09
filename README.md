# spark-tensorflow-connector


This directory contains a examples of integrating [Apache Spark](http://spark.apache.org/)
with tensorflow. This module provides importing TFRecords as SQL DataFrame and exporting DataFrame as TFRecords.

The following code snippet demonstrates the usage.



```scala
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{ DataFrame, Row }
import org.apache.spark.sql.catalyst.expressions.GenericRow
import org.apache.spark.sql.types._
import org.scalatest.{ BeforeAndAfterAll, Matchers }
import org.tensorflow.TestingSparkSessionWordSpec

val path = s"$TF_SANDBOX_DIR/output25.tfr"
val testRows: Array[Row] = Array(
new GenericRow(Array[Any](11, 1, 23L, 10.0F, 14.0, List(1.0, 2.0), "r1")),
new GenericRow(Array[Any](21, 2, 24L, 12.0F, 15.0, List(2.0, 2.0), "r2")))
val schema = StructType(List(StructField("id", IntegerType), StructField("IntegerTypelabel", IntegerType), StructField("LongTypelabel", LongType), StructField("FloatTypelabel", FloatType), StructField("DoubleTypelabel", DoubleType), StructField("vectorlabel", ArrayType(DoubleType, true)), StructField("name", StringType)))
val rdd = sparkSession.sparkContext.parallelize(testRows)

val df: DataFrame = sparkSession.createDataFrame(rdd, schema)
df.write.format("tensorflow").save(path)

val importedDf: DataFrame = sparkSession.read.format("tensorflow").load(path)
```
