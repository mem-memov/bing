package memmemov.bing

import scala.scalanative.libc.stdlib.malloc
import scala.scalanative.unsafe.UWord
import scala.scalanative.unsigned.UByte

class Page(
  private val level: Int
):

  private lazy val level = 0

  private lazy val lines = new Array[Line](256)

  private lazy val parent = new Page()

  private lazy val children = new Array[Option[Page]](256)

  def content(address: List[UByte]): UByte = ???

  def write(address: List[UByte], content: UByte): Unit = ???

  def close(): Unit = ???

object Page:

  def apply(): Page = new Page(1)