package memmemov.bing

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Memory:

  val start: Address = new Address(List(UByte.MinValue))

  private var next: Address = start
  private lazy val root: Element = new Element(new Level)

  sealed trait Append
  case class Appended(destination: Address) extends Append
  object NotAppended extends Append
  object NotAppendedContentTooBig extends Append

  def append(content: Address): Append =

    val trimmedContent = content.trimBig

    if trimmedContent > next then
      NotAppendedContentTooBig
    else
      root.write(next, trimmedContent) match
        case root.Written =>
          val destination = next
          next = next.increment
          Appended(destination)
        case root.NotWritten =>
          NotAppended

  sealed trait Update
  object Updated extends Update
  object NotUpdatedContentTooBig extends Update
  object NotUpdatedDestinationTooBig extends Update
  object NotUpdated extends Update

  def update(destination: Address, content: Address): Update =

    val trimmedDestination = destination.trimBig
    val trimmedContent = content.trimBig

    if trimmedDestination > next then
      NotUpdatedDestinationTooBig
    else
      if trimmedContent > trimmedDestination then
        NotUpdatedContentTooBig
      else
        root.write(next, content) match
          case root.Written =>
            Updated
          case root.NotWritten =>
            NotUpdated

  sealed trait Read
  case class ReadResult(content: Address) extends Read
  object NotRead extends Read

  def read(source: Address): Read =
    root.read(source) match
      case root.ReadResult(content) =>
        ReadResult(content)
      case root.NotRead =>
        NotRead

  def foreach(f: Address => Unit): Unit =

    @tailrec
    def walk(f: Address => Unit, source: Address): Unit =
      if source == next then
        ()
      else
        read(source) match
          case ReadResult(content) =>
            f(content)
            walk(f, source.increment)
          case NotRead =>
            ()

    walk(f, start)



