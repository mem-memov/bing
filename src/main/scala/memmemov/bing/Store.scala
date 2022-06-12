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
  

  def close(): Unit =

    blocks.foreach(_.close())
