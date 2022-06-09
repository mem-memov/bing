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

  def isWritten(address: Address, content: Address, count: Int, length: Int): Element.IsContentWritten = // TODO: read an address instead of a single byte
    content.foreach { i =>
      address.isWritten(block, i) match
        case Address.ContentWritten =>
          Element.ContentWritten
        case Address.ContentNotWrittenTooBig(index, shorterAddress) =>
          elements(index.toInt).isWritten(shorterAddress, paddedContent)
        case _ => Element.ContentNotWritten
    }


  def isRead(address: Address, content: Address, count: Int, length: Int): Element.IsContentRead = // TODO: read as many siblings as needed for this level
    (count until length).foldLeft(content) { case (content, _) =>
      address.isRead(block) match
        case Address.ContentRead(index) =>
          Element.ContentRead(content.prepended(index))
        case Address.ContentNotReadTooBig(index, shorterAddress) =>
          elements(index.toInt).isRead(address)
        case _ => Element.ContentNotRead
    }


object Element:

  sealed trait IsContentWritten
  object ContentWritten extends IsContentWritten
  object ContentNotWritten extends IsContentWritten

  sealed trait IsContentRead
  case class ContentRead(content: Address) extends IsContentRead
  object ContentNotRead extends IsContentRead