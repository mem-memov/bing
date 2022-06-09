package memmemov.bing.memory

import memmemov.bing.address
import memmemov.bing.element

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance:

  private var next: address.Instance = new address.Instance(List(0.toUByte))
  private val root: element.Instance = new element.Instance(0)

  def append(what: address.Instance): Append =
    val where = next
    next = next.increment

    root.write(where, what)

    Appended(
      at = where
    )





