package memmemov.bing.address

import scala.scalanative.unsigned.UByte

sealed trait Write
object Written extends Write
object NotWrittenAddressTooBig extends Write
object NotWrittenAddressEmpty extends Write
