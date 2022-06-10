package memmemov.bing.memory

import memmemov.bing.address
import memmemov.bing.element
import memmemov.bing.route

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance:

  private var next: address.Instance = new address.Instance(List(0.toUByte))
  private val root: route.Instance = new route.Instance(
    new element.Instance(0)
  )

  def start: address.Instance =
    new address.Instance(List(0.toUByte))

  def append(what: address.Instance): Append =
    if what > next then
      NotAppendedContentTooBig
    else
      val results = root.writeAddress(next, what)
      if results.nonEmpty && results.forall(_ == route.WrittenAddress) then
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
      if results.nonEmpty && results.forall(_ == route.WrittenAddress) then
        Updated
      else
        NotUpdated

  def read(where: address.Instance): Read =
    root.readAddress(where) match
      case route.ReadAddressResult(what) =>
        ReadResult(
          value = what
        )
      case route.NotReadAddress =>
        NotRead

  def foreach(f: Option[address.Instance] => Unit): Unit =

    @tailrec
    def walk(f: Option[address.Instance] => Unit, where: address.Instance): Unit =
      if where == next then
        ()
      else
        read(where) match
          case ReadResult(value) =>
            f(Some(value))
            walk(f, where.increment)
          case NotRead =>
            f(None)

    walk(f, start)



