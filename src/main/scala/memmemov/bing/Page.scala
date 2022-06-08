package memmemov.bing

import scala.scalanative.libc.stdlib.malloc
import scala.scalanative.unsafe.UWord
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Page(
  private val level: Int
):

  private lazy val lines = new Array[Line](256)

  private lazy val children = new Array[Option[Page]](256)

  def content(address: Address): Address =
    val optionalContent = address.optionalContent(lines)
    optionalContent match
      case Some(content) => Address(content) // TODO: read as many siblings as needed for this level
      case None =>
        address.shorterAddress match
          case Some((first, shorterAddress)) =>
            if children(first.toInt).isEmpty then
              children(first.toInt) = Some(new Page(level + 1))
            val optionalContent = children(first.toInt).map(_.content(shorterAddress))
            optionalContent match
              case Some(content) => content
              case None => Address(0.toUByte) // TODO: exclude this option and throw
          case None => Address(0.toUByte) // TODO: exclude this option and throw

  def write(address: Address, content: UByte): Unit = // TODO: read an address instead of a single byte
    if !address.isWritten(lines, content) then
      address.shorterAddress match
        case Some((first, shorterAddress)) =>
          if children(first.toInt).isEmpty then
            children(first.toInt) = Some(new Page(level + 1))
          children(first.toInt).map(_.write(shorterAddress, content))
        case None =>


  def close(): Unit =
    lines.foreach(_.close())

object Page:

  def apply(): Page = new Page(1)