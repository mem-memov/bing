package memmemov.bing

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Address(
  private[Address] val indices: List[UByte]
):

  private val length: Int = indices.length

  def trimBig(): Address =
    new Address(indices.dropWhile(_ == 0.toUByte))

  def padBig(n: Int): Address = ???

  def group(count: Int): List[Address] =
    List(this)

  def increment: Address =

    def plusOne(x: UByte): (UByte, Boolean) = if x == 255.toUByte then (0.toUByte, true) else ((x + 1.toUByte).toUByte, false)

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

  def equals(another: Address): Boolean =
    another.indices == this.indices

  def isWritten(block: Block, content: UByte): Address.IsContentWritten =
    length match
      case 0 =>
        Address.ContentNotWrittenTooSmall
      case 1 =>
        block.write(indices.head, content)
        Address.ContentWritten
      case _ =>
        Address.ContentNotWrittenTooBig(
          index = indices.head,
          shorterAddress = new Address(indices.tail)
        )

  def isRead(block: Block): Address.IsContentRead =
    length match
      case 0 =>
        Address.ContentNotReadAddressEmpty
      case 1 =>
        Address.ContentRead(
          content = block.content(indices.head)
        )
      case _ =>
        Address.ContentNotReadTooBig(
          index = indices.head,
          shorterAddress = new Address(indices.tail)
        )

object Address:

  def apply(indices: UByte*): Address =
    if indices.isEmpty then
      new Address(List(0.toUByte))
    else
      new Address(indices.toList)

  sealed trait IsContentWritten
  object ContentWritten extends IsContentWritten
  case class ContentNotWrittenTooBig(index: UByte, shorterAddress: Address) extends IsContentWritten
  object ContentNotWrittenTooSmall extends IsContentWritten

  sealed trait IsContentRead
  case class ContentRead(content: UByte) extends IsContentRead
  object ContentNotReadAddressEmpty extends IsContentRead
  case class ContentNotReadTooBig(index: UByte, shorterAddress: Address) extends IsContentRead