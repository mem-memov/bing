package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class PadBigSuite extends AnyFunSuite:

  test("Create another address without zero bytes at the head") {
    List(
      (
        List(UByte.MaxValue),
        0,
        List(UByte.MaxValue)
      ),
      (
        List(UByte.MaxValue),
        1,
        List(UByte.MinValue, UByte.MaxValue)
      ),
      (
        List(UByte.MaxValue),
        3,
        List(UByte.MinValue, UByte.MinValue, UByte.MinValue, UByte.MaxValue)
      ),
      (
        List(UByte.MinValue, UByte.MinValue, UByte.MinValue, UByte.MaxValue),
        3,
        List(UByte.MinValue, UByte.MinValue, UByte.MinValue, UByte.MaxValue)
      ),
      (
        List(UByte.MaxValue, UByte.MinValue),
        1,
        List(UByte.MinValue, UByte.MaxValue, UByte.MinValue)
      )
    ).foreach { case (original, n, expected) =>
      assert(
        new Instance(original).padBig(n) == new Instance(expected)
      )
    }
  }