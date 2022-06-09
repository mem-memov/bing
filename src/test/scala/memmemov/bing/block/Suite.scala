package memmemov.bing.block

import memmemov.bing.block.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.UnsignedRichInt

class Suite extends AnyFunSuite:

  test("Read a byte from a block") {
    val block = new Instance
    (
      for {
        index <- 0 to 255
        content <- 0 to 255
      } yield (index.toUByte, content.toUByte)
    ).foreach {
      case (index, content) =>
        block.write(index, content)
        val result = block.read(index)
        block.close()
        assert(result == content)
    }
  }
