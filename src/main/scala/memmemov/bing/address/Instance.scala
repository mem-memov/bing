package memmemov.bing.address

import memmemov.bing.{block, element}

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  private[Instance] val indices: List[UByte]
) extends Ordered[Instance]:

  val length: Int = indices.length

  def trimBig: Instance =
    new Instance(
      indices.dropWhile(_ == UByte.MinValue)
    )

  def padBig(n: Int): Instance =
    new Instance(
      indices.dropWhile(_ == UByte.MinValue).padTo(n, UByte.MinValue)
    )

  def increment: Instance =

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

    new Instance(resultIndices)

  override def equals(that: Any): Boolean =
    that match
      case that: Instance => compare(that) == 0
      case _ => false

  def write(where: block.Instance, what: UByte): Write =
    length match
      case 0 =>
        NotWrittenAddressEmpty
      case 1 =>
        where.write(indices.head, what)
        Written
      case _ =>
        NotWrittenAddressTooBig

  def read(where: block.Instance): Read =
    length match
      case 0 =>
        NotReadAddressEmpty
      case 1 =>
        ReadResult(
          content = where.read(indices.head)
        )
      case _ =>
        NotReadAddressTooBig

  def foreach(f: UByte => Unit): Unit = indices.foreach(f)

  def take: Take =
    if length == 0 then
      NotTakenAddressEmpty
    else
      Taken(
        index = indices.head,
        shorterAddress = new Instance(indices.tail)
      )

  override def compare(that: Instance): Int =
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
