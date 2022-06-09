package memmemov.bing.element

import scala.scalanative.unsigned.UByte

sealed trait Read
case class ReadResult(content: UByte) extends Read
object NotRead extends Read
