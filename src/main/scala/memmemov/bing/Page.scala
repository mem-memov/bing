package memmemov.bing

import scala.scalanative.libc.stdlib.malloc
import scala.scalanative.unsafe.UWord
import scala.scalanative.unsigned.UByte

class Page(
  private val level: Int
):

  private lazy val lines = new Array[Line](256)

  private lazy val children = new Array[Option[Page]](256)

  def content(address: Address): UByte = ???

  def write(address: Address, content: UByte): Unit = ???

  def close(): Unit = ???

object Page:

  def apply(): Page = new Page(1)