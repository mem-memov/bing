package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class AddressSuite extends AnyFunSuite:

  test("Address gets incremented") {
    List(
      (
        new Address(List(UByte.MinValue)),
        new Address(List(1.toUByte))
      ),
      (
        new Address(List(254.toUByte)),
        new Address(List(255.toUByte))
      ),
      (
        new Address(List(255.toUByte)),
        new Address(List(1.toUByte, UByte.MinValue))
      ),
      (
        new Address(List(255.toUByte, 255.toUByte)),
        new Address(List(1.toUByte, UByte.MinValue, UByte.MinValue))
      ),
    ).foreach{ case(original, expected) =>
      assert(
        original.increment == expected
      )
    }
  }

  test("Address provides its length") {
    (0 to 255).map(n => List.empty[UByte].padTo(n * 100, n.toUByte)).foreach { indices =>
      assert(
        new Address(indices).hasLength(indices.length)
      )
    }
  }

  test("Create another address with some zero bytes at the head") {

    val low = UByte.MinValue
    val high = UByte.MaxValue

    List(
      (
        new Address(List(high)),
        0,
        new Address(List(high)),
        Option.empty[String]
      ),
      (
        new Address(List(high)),
        3,
        new Address(List(low, low, high)),
        Option.empty[String]
      ),
      (
        new Address(List(high, low)),
        3,
        new Address(List(low, low, high, low)),
        Option.empty[String]
      ),
      (
        new Address(List(high, high, high)),
        2,
        new Address(List.empty),
        Some("NotPaddedBigAlreadyGreater")
      )
    ).foreach { case (original, target, expected, failure) =>

      original.padBig(target) match
        case original.PaddedBig(padded) =>
          assert(padded == expected)
          assert(failure.isEmpty)
        case original.NotPaddedBigAlreadyGreater =>
          assert(failure.contains("NotPaddedBigAlreadyGreater"))
    }
  }

  test("Create another address without zero bytes at the head") {

    val low = UByte.MinValue
    val high = UByte.MaxValue

    List(
      (
        new Address(List(high)),
        new Address(List(high))
      ),
      (
        new Address(List(low, high)),
        new Address(List(high))
      ),
      (
        new Address(List(low, low, low, high)),
        new Address(List(high))
      ),
      (
        new Address(List(low, high, low)),
        new Address(List(high, low))
      ),
      (
        new Address(List(high, low, low)),
        new Address(List(high, low, low))
      ),
      (
        new Address(List()),
        new Address(List())
      ),
      (
        new Address(List(low, low, low)),
        new Address(List())
      ),
    ).foreach { case (original, expected) =>
      assert(
        original.trimBig == expected
      )
    }
  }
