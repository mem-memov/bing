package memmemov.bing.element

import memmemov.bing.address

sealed trait ReadAddress
case class ReadAddressResult(value: address.Instance) extends ReadAddress
object NotReadAddress extends ReadAddress