package memmemov.bing

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

private[bing] class Store(
  private val blocks: Array[Block]
):

  sealed trait Write
  object Written extends Write
  object NotWritten extends Write

  def write(destination: UByte, content: Address): Write =

    if content.hasLength(blocks.length) then
      content.foreach { part =>
        blocks.foreach { block =>
          block.write(destination, part)
        }
      }
      Written
    else
      NotWritten

  def read(origin: UByte): Address =

    val parts = blocks.foldLeft(List.empty[UByte]) {
      case(parts, block) =>
        block.read(origin) :: parts
    }

    new Address(parts)

  def nibble(f: UByte => Unit): Unit =

    blocks.foreach { block =>
      (UByte.MinValue.toInt to UByte.MaxValue.toInt).view.foreach { origin =>
        f(block.read(origin.toUByte))
      }
    }
    
  var blockCursor = 0
  var destinationCursor = 0
  val blockLimit = blocks.length - 1
  val destinationLimit = 255

  def feed(content: UByte): Boolean =
    
    if blockCursor == blockLimit && destinationCursor == destinationLimit then
      false
    else
      blocks(blockCursor).write(destinationCursor.toUByte, content)
      if destinationCursor == destinationLimit then
        if blockCursor != blockLimit then
          blockCursor = blockCursor + 1
          destinationCursor = 0
      else
        destinationCursor = destinationCursor + 1
      true

  def close(): Unit =

    blocks.foreach(_.close())
