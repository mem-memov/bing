package memmemov.bing

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Stock(
  private val elements: Array[Element]
):

  sealed trait Write
  object Written extends Write
  object NotWritten extends Write

  def write(index: UByte, destination: Address, content: Address): Write =

    val element = elements(index.toInt)
    element.write(destination, content) match
      case element.Written =>
        Written
      case element.NotWritten =>
        NotWritten

  sealed trait Read
  case class ReadResult(content: Address) extends Read
  object NotRead extends Read

  def read(index: UByte, origin: Address) =

    val element = elements(index.toInt)
    element.read(origin) match
      case element.ReadResult(content) =>
        ReadResult(content)
      case element.NotRead =>
        NotRead

  def close(): Unit =

    elements.foreach(_.close())