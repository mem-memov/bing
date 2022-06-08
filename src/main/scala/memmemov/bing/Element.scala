package memmemov.bing

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Element(
  private val level: Int
):
  private lazy val block: Block = new Block
  private lazy val elements: Array[Element] = (new Array[Element](256)).map(_ => new Element(level + 1))

  def close(): Unit =
    block.close()
    elements.foreach(_.close())

  def isWritten(address: Address, content: UByte): Element.IsContentWritten = // TODO: read an address instead of a single byte
    address.isWritten(block, content) match
      case Address.ContentWritten =>
        Element.ContentWritten
      case Address.ContentNotWrittenTooBig(index, shorterAddress) =>
        elements(index.toInt).isWritten(shorterAddress, content)
      case _ => Element.ContentNotWritten

  def isRead(address: Address): Element.IsContentRead = // TODO: read as many siblings as needed for this level
    address.isRead(block) match
      case Address.ContentRead(content) =>
        Element.ContentRead(content)
      case Address.ContentNotReadTooBig(index, shorterAddress) =>
        elements(index.toInt).isRead(address)
      case _ => Element.ContentNotRead

object Element:

  sealed trait IsContentWritten
  object ContentWritten extends IsContentWritten
  object ContentNotWritten extends IsContentWritten

  sealed trait IsContentRead
  case class ContentRead(content: UByte) extends IsContentRead
  object ContentNotRead extends IsContentRead