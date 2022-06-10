package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class TrimBigSuite extends AnyFunSuite:

  test("Create another address without zero bytes at the head") {
    List(
      (
        List(UByte.MaxValue),
        List(UByte.MaxValue)
      ),
      (
        List(UByte.MinValue, UByte.MaxValue),
        List(UByte.MaxValue)
      ),
      (
        List(UByte.MinValue, UByte.MinValue, UByte.MinValue, UByte.MaxValue),
        List(UByte.MaxValue)
      ),
      (
        List(UByte.MinValue, UByte.MaxValue, UByte.MinValue),
        List(UByte.MaxValue, UByte.MinValue)
      ),
      (
        List(UByte.MaxValue, UByte.MinValue, UByte.MinValue),
        List(UByte.MaxValue, UByte.MinValue, UByte.MinValue)
      ),
    ).foreach { case (original, expected) =>
      assert(
        new Instance(original).trimBig == new Instance(expected)
      )
    }
  }