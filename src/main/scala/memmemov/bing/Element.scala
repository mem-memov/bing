package memmemov.bing

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Element(
  private val level: Level
):

  private lazy val store: Store = level.createStore

  private lazy val stock: Stock = level.createStock

  sealed trait Write
  object Written extends Write
  object NotWritten extends Write

  def write(destination: Address, content: Address): Write =

    destination.shorten match
      case destination.NotShortened =>
        NotWritten
      case destination.Shortened(destinationPart, shorterDestination) =>
        if shorterDestination.isEmpty then
          store.write(destinationPart, content) match
            case store.Written =>
              Written
            case store.NotWritten =>
              NotWritten
        else
          stock.write(destinationPart, shorterDestination, content) match
            case stock.Written =>
              Written
            case stock.NotWritten =>
              NotWritten

  sealed trait Read
  case class ReadResult(content: Address) extends Read
  object NotRead extends Read

  def read(origin: Address): Read =

    origin.shorten match
      case origin.NotShortened =>
        NotRead
      case origin.Shortened(originPart, shorterOrigin) =>
        if shorterOrigin.isEmpty then
          val content = store.read(originPart)
          ReadResult(content)
        else
          stock.read(originPart, shorterOrigin) match
            case stock.ReadResult(content) =>
              ReadResult(content)
            case stock.NotRead =>
              NotRead

  def close(): Unit =

    store.close()
    stock.close()

