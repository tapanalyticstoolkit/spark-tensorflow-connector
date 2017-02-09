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

import org.tensorflow.DataTypesConvertor
import org.tensorflow.example.{ BytesList, FloatList, Int64List, Feature }
import org.tensorflow.hadoop.shaded.protobuf.ByteString
import scala.util.{ Failure, Success, Try }

class TypeConversionException(val message: String, cause: Throwable = null) extends Exception(message, cause)

trait FeatureEncoder {
  def encode(value: Any): Feature
}

object Int64ListFeatureEncoder extends FeatureEncoder {
  override def encode(value: Any): Feature = {
    try {
      val int64List = value match {
        case i: Int => Int64List.newBuilder().addValue(i.toLong).build()
        case l: Long => Int64List.newBuilder().addValue(l).build()
        case arr: scala.collection.mutable.WrappedArray[_] => toInt64List(arr.toArray)
        case arr: Array[Any] => toInt64List(arr)
        case _ => throw new RuntimeException(s"Cannot convert object $value to Int64List")
      }
      Feature.newBuilder().setInt64List(int64List).build()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert object $value of type ${value.getClass} to Int64List feature.", ex)
      }
    }
  }

  private def toInt64List(arr: Array[Any]): Int64List = {
    val intListBuilder = Int64List.newBuilder()
    arr.foreach(x => {
      val longValue = DataTypesConvertor.toLong(x)
      intListBuilder.addValue(longValue)
    })
    intListBuilder.build()
  }
}

object FloatListFeatureEncoder extends FeatureEncoder {
  override def encode(value: Any): Feature = {
    try {
      val floatList = value match {
        case f: Float => FloatList.newBuilder().addValue(f).build()
        case d: Double => FloatList.newBuilder().addValue(d.toFloat).build()
        case arr: scala.collection.mutable.WrappedArray[_] => toFloatList(arr.toArray)
        case arr: Array[Any] => toFloatList(arr)
        case _ => throw new RuntimeException(s"Cannot convert object $value to FloatList")
      }
      Feature.newBuilder().setFloatList(floatList).build()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert object $value of type ${value.getClass} to FloatList feature.", ex)
      }
    }
  }

  private def toFloatList(arr: Array[Any]): FloatList = {
    val floatListBuilder = FloatList.newBuilder()
    arr.foreach(x => {
      val longValue = DataTypesConvertor.toFloat(x)
      floatListBuilder.addValue(longValue)
    })
    floatListBuilder.build()
  }
}

object ByteListFeatureEncoder extends FeatureEncoder {
  override def encode(value: Any): Feature = {
    try {
      val byteList = BytesList.newBuilder().addValue(ByteString.copyFrom(value.toString.getBytes)).build()
      Feature.newBuilder().setBytesList(byteList).build()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert object $value of type ${value.getClass} to ByteList feature.", ex)
      }
    }
  }
}

