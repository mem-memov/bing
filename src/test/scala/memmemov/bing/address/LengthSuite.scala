package memmemov.bing.address

import memmemov.bing.address.Instance
import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class LengthSuite extends AnyFunSuite:

  test("Address provides its length") {
    (0 to 255).map(n => List.empty[UByte].padTo(n * 100, n.toUByte)).foreach { indices =>
      assert(
        new Instance(indices).length == indices.length
      )
    }
  }