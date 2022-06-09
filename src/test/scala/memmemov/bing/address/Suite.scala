package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.UnsignedRichInt

class Suite extends AnyFunSuite:

  test("Address gets incremented") {
    List(
      (
        List(0.toUByte),
        List(1.toUByte)
      ),
      (
        List(254.toUByte),
        List(255.toUByte)
      ),
      (
        List(255.toUByte),
        List(1.toUByte, 0.toUByte)
      ),
      (
        List(255.toUByte, 255.toUByte),
        List(1.toUByte, 0.toUByte, 0.toUByte)
      ),
    ).foreach{ case(original, expected) =>
      val address = new Instance(original)
      assert(address.increment == new Instance(expected))
    }
  }