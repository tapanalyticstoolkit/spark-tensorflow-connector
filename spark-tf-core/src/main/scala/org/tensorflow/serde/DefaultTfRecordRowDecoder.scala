/**
 *  Copyright (c) 2016 Intel Corporation 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tensorflow.serde

import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.tensorflow.example._
import scala.collection.JavaConverters._

trait TfRecordRowDecoder {
  /**
   * Decodes each TensorFlow "Example" as Frame "Row"
   *
   * Maps each feature in Example to element in Row with DataType based on custom schema or default mapping of Int64List, FloatList, BytesList to column data type
   *
   * @param example TensorFlow Example to decode
   * @param schema Decode Example using specified schema
   * @return a frame row
   */
  def decodeTfRecord(example: Example, schema: StructType): Row
}

object DefaultTfRecordRowDecoder extends TfRecordRowDecoder {

  def decodeTfRecord(example: Example, schema: StructType): Row = {
    val row = Array.fill[Any](schema.length)(null)
    example.getFeatures.getFeatureMap.asScala.foreach {
      case (featureName, feature) =>
        val index = schema.fieldIndex(featureName)
        val colDataType = schema.fields(index).dataType
        row(index) = colDataType match {
          case dtype if dtype.equals(IntegerType) | dtype.equals(LongType) => {
            val dataList = feature.getInt64List.getValueList
            if (dataList.size() == 1) {
              val p = dataList.get(0)
              p.intValue()
            }
            else
              throw new RuntimeException("Mismatch in schema type, expected int32 or int64")
          }
          case dtype if dtype.equals(FloatType) | dtype.equals(DoubleType) => {
            val dataList = feature.getFloatList.getValueList
            if (dataList.size() == 1) {
              val p = dataList.get(0)
              p.floatValue()
            }
            else
              throw new RuntimeException("Mismatch in schema type, expected float32 or float64")
          }
          case vtype: ArrayType => {
            feature.getKindCase.getNumber match {
              case Feature.INT64_LIST_FIELD_NUMBER => {
                val datalist = feature.getInt64List.getValueList.asScala.toList
                datalist
              }
              case Feature.FLOAT_LIST_FIELD_NUMBER => {
                val datalist = feature.getFloatList.getValueList.asScala.toList
                datalist
              }
            }
          }
          case dtype if dtype.equals(StringType) => feature.getBytesList.toByteString.toStringUtf8.trim
          case _ => throw new RuntimeException("Unsupported dataype...!!")
        }
    }
    Row.fromSeq(row)
  }
}
