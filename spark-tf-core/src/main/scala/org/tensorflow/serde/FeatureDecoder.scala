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

import org.tensorflow.example.Feature
import scala.collection.JavaConverters._

trait FeatureDecoder[T] {
  def decode(feature: Feature): T
}

object IntFeatureDecoder extends FeatureDecoder[Int] {
  override def decode(feature: Feature): Int = {
    try {
      val int64List = feature.getInt64List.getValueList
      require(int64List.size() == 1, "Length of Int64List must equal 1")
      int64List.get(0).intValue()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Int.", ex)
      }
    }
  }
}

object LongFeatureDecoder extends FeatureDecoder[Long] {
  override def decode(feature: Feature): Long = {
    try {
      val int64List = feature.getInt64List.getValueList
      require(int64List.size() == 1, "Length of Int64List must equal 1")
      int64List.get(0).longValue()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Long.", ex)
      }
    }
  }
}

object FloatFeatureDecoder extends FeatureDecoder[Float] {
  override def decode(feature: Feature): Float = {
    try {
      val floatList = feature.getFloatList.getValueList
      require(floatList.size() == 1, "Length of FloatList must equal 1")
      floatList.get(0).floatValue()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Float.", ex)
      }
    }
  }
}

object DoubleFeatureDecoder extends FeatureDecoder[Double] {
  override def decode(feature: Feature): Double = {
    try {
      val floatList = feature.getFloatList.getValueList
      require(floatList.size() == 1, "Length of FloatList must equal 1")
      floatList.get(0).doubleValue()
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Double.", ex)
      }
    }
  }
}

object IntArrayFeatureDecoder extends FeatureDecoder[Array[Int]] {
  override def decode(feature: Feature): Array[Int] = {
    try {
      val array = feature.getInt64List.getValueList.asScala.toArray
      array.map(_.toInt)
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Array[Int].", ex)
      }
    }
  }
}

object LongArrayFeatureDecoder extends FeatureDecoder[Array[Long]] {
  override def decode(feature: Feature): Array[Long] = {
    try {
      val array = feature.getInt64List.getValueList.asScala.toArray
      array.map(_.toLong)
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Array[Long].", ex)
      }
    }
  }
}

object FloatArrayFeatureDecoder extends FeatureDecoder[Array[Float]] {
  override def decode(feature: Feature): Array[Float] = {
    try {
      val array = feature.getFloatList.getValueList.asScala.toArray
      array.map(_.toFloat)
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Array[Float].", ex)
      }
    }
  }
}

object DoubleArrayFeatureDecoder extends FeatureDecoder[Array[Double]] {
  override def decode(feature: Feature): Array[Double] = {
    try {
      val array = feature.getFloatList.getValueList.asScala.toArray
      array.map(_.toDouble)
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to Array[Double].", ex)
      }
    }
  }
}

object StringFeatureDecoder extends FeatureDecoder[String] {
  override def decode(feature: Feature): String = {
    try {
      feature.getBytesList.toByteString.toStringUtf8.trim
    }
    catch {
      case ex: Exception => {
        throw new TypeConversionException(s"Cannot convert feature to String.", ex)
      }
    }
  }
}
