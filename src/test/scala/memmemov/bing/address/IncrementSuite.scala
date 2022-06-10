package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class IncrementSuite extends AnyFunSuite:

  test("Address gets incremented") {
    List(
      (
        new Instance(List(UByte.MinValue)),
        new Instance(List(1.toUByte))
      ),
      (
        new Instance(List(254.toUByte)),
        new Instance(List(255.toUByte))
      ),
      (
        new Instance(List(255.toUByte)),
        new Instance(List(1.toUByte, UByte.MinValue))
      ),
      (
        new Instance(List(255.toUByte, 255.toUByte)),
        new Instance(List(1.toUByte, UByte.MinValue, UByte.MinValue))
      ),
    ).foreach{ case(original, expected) =>
      assert(
        original.increment == expected
      )
    }
  }