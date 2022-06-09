package memmemov.bing.memory

import memmemov.bing.address

sealed trait Read
case class ReadResult(value: address.Instance) extends Read
object NotRead extends Read