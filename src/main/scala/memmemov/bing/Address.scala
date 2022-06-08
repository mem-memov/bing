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

  def optionalContent(lines: Array[Line]): Option[UByte] =
    if length == 2 then
      val first = indices.head
      val second = indices.drop(1).head
      Option(lines(first.toInt).content(second))
    else
      Option.empty

  def isWritten(lines: Array[Line], content: UByte): Boolean =
    if length == 2 then
      val first = indices.head
      val second = indices.drop(1).head
      lines(first.toInt).write(second, content)
      true
    else
      false
      
  def shorterAddress: Option[(UByte, Address)] =
    if length <= 1 then
      None
    else
      Option((indices.head, new Address(indices.tail)))


object Address:

  def apply(indices: UByte*): Address =
    if indices.isEmpty then
      new Address(List(0.toUByte))
    else
      new Address(indices.toList)