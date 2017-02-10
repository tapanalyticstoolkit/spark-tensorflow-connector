# spark-tensorflow-connector

This repo contains a library for loading and storing TensorFlow records with [Apache Spark](http://spark.apache.org/). The library implements data import from the standard TensorFlow record format ([TFRecords]( https://www.tensorflow.org/how_tos/reading_data/)) into Spark SQL DataFrames, and data export from DataFrames to TensorFlow records.

## What's new

This is the initial release of the `spark-tensorflow-connector` repo.

## Known issues

None.

## Prerequisites

1. [Apache Spark 2.0 (or later)](http://spark.apache.org/)

2. [Apache Maven](https://maven.apache.org/)

The following code snippet demonstrates usage.

```scala
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{ DataFrame, Row }
import org.apache.spark.sql.catalyst.expressions.GenericRow
import org.apache.spark.sql.types._

val path = s"$TF_SANDBOX_DIR/output25.tfr"
val testRows: Array[Row] = Array(
new GenericRow(Array[Any](11, 1, 23L, 10.0F, 14.0, List(1.0, 2.0), "r1")),
new GenericRow(Array[Any](21, 2, 24L, 12.0F, 15.0, List(2.0, 2.0), "r2")))
val schema = StructType(List(StructField("id", IntegerType), StructField("IntegerTypelabel", IntegerType), StructField("LongTypelabel", LongType), StructField("FloatTypelabel", FloatType), StructField("DoubleTypelabel", DoubleType), StructField("vectorlabel", ArrayType(DoubleType, true)), StructField("name", StringType)))
val rdd = spark.sparkContext.parallelize(testRows)

//Save DataFrame as TFRecords
val df: DataFrame = spark.createDataFrame(rdd, schema)
df.write.format("tensorflow").save(path)

//Read TFRecords into DataFrame.
//The DataFrame schema is automatically inferred from the TFRecords.
val importedDf1: DataFrame = spark.read.format("tensorflow").load(path)
importedDf1.show()

//Provide custom schema while loading
val importedDf: DataFrame = spark.read.format("tensorflow").schema(schema).load(path)
importedDf.show()

```
