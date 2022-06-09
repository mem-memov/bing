package memmemov.bing.block

import scala.scalanative.libc.stdlib.{free, malloc}
import scala.scalanative.unsafe.{Ptr, UWord}
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance:

  private lazy val bytePointer: Ptr[UByte] = malloc(256.toUByte).asInstanceOf[Ptr[UByte]]
  
  def read(where: UByte): UByte = bytePointer(where)
  
  def write(where: UByte, what: UByte): Unit = bytePointer.update(where, what)
  
  def close(): Unit = free(bytePointer.asInstanceOf[Ptr[Byte]])
