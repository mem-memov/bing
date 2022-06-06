package memmemov.bing

import scala.scalanative.libc.stdlib.{free, malloc}
import scala.scalanative.unsafe.{Ptr, UWord}
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Line():

  private lazy val bytePointer: Ptr[UByte] = malloc(256.toUByte).asInstanceOf[Ptr[UByte]]
  
  def content(index: UByte): UByte = bytePointer(index)
  
  def write(index: UByte, content: UByte): Unit = bytePointer.update(index, content)
  
  def close(): Unit = free(bytePointer.asInstanceOf[Ptr[Byte]])
