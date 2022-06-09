package memmemov.bing.address

import memmemov.bing.{block, element}

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  private[Instance] val indices: List[UByte]
):

  private val length: Int = indices.length

  def trimBig(): Instance =
    new Instance(indices.dropWhile(_ == 0.toUByte))

  def padBig(n: Int): Instance =
    new Instance(indices.padTo(n, 0.toUByte))

  def group(count: Int): List[Instance] =
    List(this)

  def prepended(index: UByte): Instance =
    new Instance(index :: indices)

  def increment: Instance =

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

    new Instance(resultIndices)

  def equals(another: Instance): Boolean =
    another.indices == this.indices

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

  def map[A](f: UByte => A): List[A] = indices.map(f)

  def take: Take =
    if length == 0 then
      NotTakenAddressEmpty
    else
      Taken(
        index = indices.head,
        shorterAddress = new Instance(indices.tail)
      )
