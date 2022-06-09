package memmemov.bing.element

import memmemov.bing.address
import memmemov.bing.block

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  private val level: Int
):
  private lazy val content: block.Instance = new block.Instance
  private lazy val children: Array[Instance] = (new Array[Instance](256)).map(_ => new Instance(level + 1))

  def close(): Unit =
    content.close()
    children.foreach(_.close())

  def write(where: address.Instance, what: UByte): Write =
    where.write(content, what) match
      case address.Written =>
        Written
      case address.NotWrittenAddressTooBig =>
        where.take match
          case address.Taken(index, shorterAddress) =>
            children(index.toInt).write(shorterAddress, what)
          case _ => NotWritten
      case _ => NotWritten


  def read(where: address.Instance): Read =
    where.read(content) match
      case address.ReadResult(what) =>
        ReadResult(what)
      case address.NotReadAddressTooBig =>
        where.take match
          case address.Taken(index, shorterAddress) =>
            children(index.toInt).read(where)
          case _ => NotRead
      case _ => NotRead

  def write(where: address.Instance, what: address.Instance): Unit =
    what.take match
      case address.NotTakenAddressEmpty => ()
      case address.Taken(index, shorterAddress) =>
        this.write(where, index) match
          case NotWritten => ()
          case Written =>
            write(where.increment, shorterAddress)