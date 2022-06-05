package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.UnsignedRichInt

class LineSuite extends AnyFunSuite:

  test("Read a Byte from Line") {
    val line = Line(0.toULong, 1.toULong)
    line.written(0.toULong, 12.toByte)
    val readByte = line.optionalByte(0.toULong)
    assert(readByte.contains(12.toByte))
  }
