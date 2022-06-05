package memmemov.bing

import scala.scalanative.unsafe.{Ptr, UWord}
import scala.scalanative.unsigned.UnsignedRichInt
import scala.scalanative.libc.stdlib.{free, malloc}

class Line(
  private val firstIndex: UWord,
  private val size: UWord
):

  private var optionalBytePointer: Option[Ptr[Byte]] = None

  private val lastIndex = firstIndex + size - 1.toULong


  private def optionalIndex(index: UWord): Option[UWord] =

    if index >= firstIndex && index <= lastIndex then
      Some(index - firstIndex)
    else
      None


  def optionalByte(index: UWord): Option[Byte] =

    for {
      i <- optionalIndex(index)
      p <- optionalBytePointer
    } yield p(i)


  def written(index: UWord, byte: Byte): Boolean =

    val optionalUpdateResult = for {
      i <- optionalIndex(index)
      p <- optionalBytePointer
    } yield p.update(i, byte)

    optionalUpdateResult.isDefined


  def open(): Unit =

    optionalBytePointer match
      case None => optionalBytePointer = Some(malloc(size))
      case _ => ()


  def close(): Unit =

    optionalBytePointer match
      case None => ()
      case Some(p) => free(p)

    optionalBytePointer = None


  def isOpen(): Boolean =

    optionalBytePointer.isDefined


  def isClosed(): Boolean =

    optionalBytePointer.isEmpty


object Line:

  def apply(first: UWord, size: UWord) =

    new Line(first, size)


