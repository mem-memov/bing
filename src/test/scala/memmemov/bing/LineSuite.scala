package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.UnsignedRichInt

class LineSuite extends AnyFunSuite:

  test("Read a byte from a line") {
    val line = Line()
    (
      for {
        index <- 0 to 255
        content <- 0 to 255
      } yield (index.toUByte, content.toUByte)
    ).foreach {
      case (index, content) =>
        line.write(index, content)
        val result = line.content(index)
        line.close()
        assert(result == content)
    }
  }
