package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite
import scala.scalanative.unsigned.UnsignedRichInt

class AddressSuite extends AnyFunSuite:

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
      val address = new Address(original)
      assert(address.increment == new Address(expected))
    }
  }