package memmemov.bing.address

import scala.scalanative.unsigned.UByte

sealed trait Read
case class ReadResult(content: UByte) extends Read
object NotReadAddressEmpty extends Read
object NotReadAddressTooBig extends Read
