package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class TrimBigSuite extends AnyFunSuite:

  private val low = UByte.MinValue
  private val high = UByte.MinValue
  
  test("Create another address without zero bytes at the head") {
    List(
      (
        new Instance(List(high)),
        new Instance(List(high))
      ),
      (
        new Instance(List(low, high)),
        new Instance(List(high))
      ),
      (
        new Instance(List(low, low, low, high)),
        new Instance(List(high))
      ),
      (
        new Instance(List(low, high, low)),
        new Instance(List(high, low))
      ),
      (
        new Instance(List(high, low, low)),
        new Instance(List(high, low, low))
      ),
      (
        new Instance(List()),
        new Instance(List())
      ),
      (
        new Instance(List(low, low, low)),
        new Instance(List())
      ),
    ).foreach { case (original, expected) =>
      assert(
        original.trimBig == expected
      )
    }
  }