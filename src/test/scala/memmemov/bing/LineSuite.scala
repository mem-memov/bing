package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.UnsignedRichInt

class LineSuite extends AnyFunSuite:

  test("Read a Byte from Line") {
    val line = Line()
    val index = 0.toUByte
    val content = 12.toUByte
    line.write(index, content)
    val result = line.content(index)
    line.close()
    assert(result == content)
  }
