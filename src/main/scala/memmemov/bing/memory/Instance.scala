package memmemov.bing.memory

import memmemov.bing.address
import memmemov.bing.element

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance:

  private var next: address.Instance = new address.Instance(List(0.toUByte))
  private val root: element.Instance = new element.Instance(0)

  def append(what: address.Instance): Append =
    if what > next then
      NotAppendedContentTooBig
    else
      val results = root.writeAddress(next, what)
      if results.nonEmpty && results.forall(_ == element.WrittenAddress) then
        val where = next
        next = next.increment
        Appended(
          at = where
        )
      else
        NotAppended

  def update(where: address.Instance, what: address.Instance): Update =
    if what > where then
      NotUpdatedContentTooBig
    else
      val results = root.writeAddress(next, what)
      if results.nonEmpty && results.forall(_ == element.WrittenAddress) then
        Updated
      else
        NotUpdated

  def read(where: address.Instance): Read =
    root.readAddress(where) match
      case element.ReadAddressResult(what) =>
        ReadResult(
          value = what
        )
      case element.NotReadAddress =>
        NotRead


