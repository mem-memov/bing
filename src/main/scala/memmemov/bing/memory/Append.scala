package memmemov.bing.memory

import memmemov.bing.address

sealed trait Append
case class Appended(where: address.Instance) extends Append
object NotAppended extends Append
object NotAppendedContentTooBig extends Append