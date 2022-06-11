package memmemov.bing

import scala.scalanative.libc.stdlib.{free, malloc}
import scala.scalanative.unsafe.{CSize, Ptr, UWord}
import scala.scalanative.unsigned.{UByte, UnsignedRichInt, UnsignedRichLong}

class Block:

  private lazy val bytePointer: Ptr[UByte] = malloc(Block.size).asInstanceOf[Ptr[UByte]]
  
  def read(source: UByte): UByte = bytePointer(source)
  
  def write(destination: UByte, content: UByte): Unit = bytePointer.update(destination, content)
  
  def close(): Unit = free(bytePointer.asInstanceOf[Ptr[Byte]])

object Block:

  private lazy val size: CSize = UByte.MaxValue.toULong + 1.toULong