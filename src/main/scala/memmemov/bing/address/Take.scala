package memmemov.bing.address

import scala.scalanative.unsigned.UByte

sealed trait Take
object NotTakenAddressEmpty extends Take
case class Taken(index: UByte, shorterAddress: Instance) extends Take
