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
package org.tensorflow

import org.slf4j.LoggerFactory
import scala.language.implicitConversions

/**
 * DataTypes supported
 */
object DataTypesConvertor {

  lazy val logger = LoggerFactory.getLogger("DataTypesConvertor")
  /**
   * Attempt to cast Any type to Double
   *
   * @param value input Any type to be cast
   * @return value cast as Double, if possible
   */
  def toDouble(value: Any): Double = {
    value match {
      case null => throw new IllegalArgumentException("null cannot be converted to Double")
      case i: Int => i.toDouble
      case l: Long => l.toDouble
      case f: Float => f.toDouble
      case d: Double => d
      case bd: BigDecimal => bd.toDouble
      case s: String => s.trim().toDouble
      case _ => throw new IllegalArgumentException(s"The following value is not a numeric data type: $value")
    }
  }

  def toBigDecimal(value: Any): BigDecimal = {
    value match {
      case null => null
      case i: Int => BigDecimal(i)
      case l: Long => BigDecimal(l)
      case f: Float => BigDecimal(f)
      case d: Double => BigDecimal(d)
      case bd: BigDecimal => bd
      case s: String => BigDecimal(s)
      case _ => throw new IllegalArgumentException(s"The following value is not of numeric data type: $value")
    }
  }

  def toLong(value: Any): Long = {
    value match {
      case null => throw new IllegalArgumentException("null cannot be converted to Long")
      case i: Int => i.toLong
      case l: Long => l
      case f: Float => f.toLong
      case d: Double => d.toLong
      case bd: BigDecimal => bd.toLong
      case s: String => s.trim().toLong
      case _ => throw new RuntimeException(s"${value.getClass.getName} toLong is not implemented")
    }
  }

  def toInt(value: Any): Int = {
    value match {
      case null => throw new IllegalArgumentException("null cannot be converted to Int")
      case i: Int => i
      case l: Long => l.toInt
      case f: Float => f.toInt
      case d: Double => d.toInt
      case bd: BigDecimal => bd.toInt
      case s: String => s.trim().toInt
      case _ => throw new RuntimeException(s"${value.getClass.getName} toInt is not implemented")
    }
  }

  def toFloat(value: Any): Float = {
    value match {
      case null => throw new IllegalArgumentException("null cannot be converted to Float")
      case i: Int => i.toFloat
      case l: Long => l.toFloat
      case f: Float => f
      case d: Double => d.toFloat
      case bd: BigDecimal => bd.toFloat
      case s: String => s.trim().toFloat
      case _ => throw new RuntimeException(s"${value.getClass.getName} toFloat is not implemented")
    }
  }

  def toStr(value: Any): String = {
    value match {
      case null => null
      case s: String => s
      case value =>
        try {
          value.toString
        }
        catch {
          case e: Exception => throw new RuntimeException(s"${value.getClass.getName} toStr is not implemented")
        }
    }
  }
}

