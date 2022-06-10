package memmemov.bing.block

import scala.scalanative.libc.stdlib.{free, malloc}
import scala.scalanative.unsafe.{CSize, Ptr, UWord}
import scala.scalanative.unsigned.{UByte, UnsignedRichInt, UnsignedRichLong}

class Instance:

  private lazy val bytePointer: Ptr[UByte] = malloc(Instance.size).asInstanceOf[Ptr[UByte]]
  
  def read(where: UByte): UByte = bytePointer(where)
  
  def write(where: UByte, what: UByte): Unit = bytePointer.update(where, what)
  
  def close(): Unit = free(bytePointer.asInstanceOf[Ptr[Byte]])

object Instance:

  private lazy val size: CSize = UByte.MaxValue.toULong + 1.toULong
