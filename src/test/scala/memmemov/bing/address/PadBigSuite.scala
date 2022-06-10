package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class PadBigSuite extends AnyFunSuite:
  
  private val low = UByte.MinValue
  private val high = UByte.MinValue

  test("Create another address with some zero bytes at the head") {
    List(
      (
        new Instance(List(high)),
        new Instance(List(high)),
        PaddedBig(new Instance(List(high)))
      ),
      (
        new Instance(List(high)),
        new Instance(List(high, high)),
        PaddedBig(new Instance(List(low, high)))
      ),
      (
        new Instance(List(high)),
        new Instance(List(high, high, high, high)),
        PaddedBig(new Instance(List(low, low, low, high)))
      ),
      (
        new Instance(List(high, high)),
        new Instance(List(high)),
        NotPaddedBigAlreadyGreater
      ),
      (
        new Instance(List()),
        new Instance(List()),
        PaddedBig(new Instance(List()))
      ),
      (
        new Instance(List()),
        new Instance(List(high, high, high, high)),
        PaddedBig(new Instance(List(low, low, low, low)))
      ),
      (
        new Instance(List()),
        new Instance(List(low, low, low, high)),
        PaddedBig(new Instance(List(low)))
      )
    ).foreach { case (original, example, expected) =>
      assert(
        original.padBig(example) == expected
      )
    }
  }