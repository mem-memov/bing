package memmemov.bing.memory

import memmemov.bing.address

sealed trait Append
case class Appended(at: address.Instance) extends Append
object NotAppended extends Append