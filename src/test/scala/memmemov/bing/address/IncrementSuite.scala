package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class IncrementSuite extends AnyFunSuite:

  test("Address gets incremented") {
    List(
      (
        List(UByte.MinValue),
        List(1.toUByte)
      ),
      (
        List(254.toUByte),
        List(255.toUByte)
      ),
      (
        List(255.toUByte),
        List(1.toUByte, UByte.MinValue)
      ),
      (
        List(255.toUByte, 255.toUByte),
        List(1.toUByte, UByte.MinValue, UByte.MinValue)
      ),
    ).foreach{ case(original, expected) =>
      assert(
        new Instance(original).increment == new Instance(expected)
      )
    }
  }