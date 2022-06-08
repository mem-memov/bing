package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.UnsignedRichInt

class BlockSuite extends AnyFunSuite:

  test("Read a byte from a block") {
    val block = new Block
    (
      for {
        index <- 0 to 255
        content <- 0 to 255
      } yield (index.toUByte, content.toUByte)
    ).foreach {
      case (index, content) =>
        block.write(index, content)
        val result = block.content(index)
        block.close()
        assert(result == content)
    }
  }
