package memmemov.bing

import scala.scalanative.unsafe.{UWord, Ptr}

class Line(
  private val first: UWord,
  private val size: UWord,
  private val bytePointer: Ptr[Byte]
):

  private val last = first + size - 1.asInstanceOf[UWord]

  private def inRange(index: UWord) = index >= first || index <= last

  def read(index: UWord): Option[Byte] = if inRange(index) then Some(bytePointer(index)) else None



object Line:

  def apply(first: UWord, size: UWord, bytePointer: Ptr[Byte]) = new Line(first, size, bytePointer)


