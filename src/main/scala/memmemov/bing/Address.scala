package memmemov.bing

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Address(
  private[Address] val indices: List[UByte]
) extends Entry with Ordered[Address]:

  private[Address] lazy val length: Int = indices.length

  private[bing] def trimBig: Address =
    new Address(
      indices.dropWhile(_ == UByte.MinValue)
    )

  private[bing] sealed trait PadBig
  private[bing] case class PaddedBig(padded: Address) extends PadBig
  private[bing] object NotPaddedBigAlreadyGreater extends PadBig

  private[bing] def padBig(target: Int): PadBig =

    if length == target then
      PaddedBig(this)
    else
      val trimmed = this.trimBig
      if trimmed.length > target then
        NotPaddedBigAlreadyGreater
      else
        PaddedBig(
          padded = new Address(
            trimmed.indices.padTo(target, UByte.MinValue)
          )
        )

  def hasLength(length: Int): Boolean =
    this.length == length

  def increment: Address =

    def plusOne(x: UByte): (UByte, Boolean) = if x == 255.toUByte then (UByte.MinValue, true) else ((x + 1.toUByte).toUByte, false)

    val (accumulator, _, hasOverflow) = indices.reverse.foldLeft((List.empty[UByte], true, false)) {
      case ((accumulator, isStart, hasOverflow), index) =>
        if isStart then
          val (incrementedIndex, overflow) = plusOne(index)
          (incrementedIndex :: accumulator, false, overflow)
        else
          if hasOverflow then
            val (incrementedIndex, overflow) = plusOne(index)
            (incrementedIndex :: accumulator, false, overflow)
          else
            (index :: accumulator, false, false)
    }

    val resultIndices = if hasOverflow then 1.toUByte :: accumulator.reverse else accumulator.reverse

    new Address(resultIndices)

  override def foreach(f: UByte => Unit): Unit = indices.foreach(f)

  private[bing] sealed trait Shorten
  private[bing] object NotShortened extends Shorten
  private[bing] case class Shortened(addressPart: UByte, shorterAddress: Address) extends Shorten

  private[bing] def shorten: Shorten =
    if length == 0 then
      NotShortened
    else
      Shortened(
        addressPart = indices.head,
        shorterAddress = new Address(indices.tail)
      )

  def isEmpty: Boolean =
    length == 0

  override def compare(that: Address): Int =
    val trimmedThis = this.trimBig
    val trimmedThat = that.trimBig
    if trimmedThis.length != trimmedThat.length then
      trimmedThis.length - trimmedThat.length
    else
      trimmedThis.indices.zipAll(trimmedThat.indices, UByte.MinValue, UByte.MinValue)
        .dropWhile { case (thisIndex, thatIndex) =>
          thisIndex == thatIndex
        } match
          case Nil => 0
          case (thisIndex, thatIndex) :: _ => if thisIndex > thatIndex then 1 else -1

  override def equals(that: Any): Boolean =
    that match
      case that: Address => compare(that) == 0
      case _ => false
