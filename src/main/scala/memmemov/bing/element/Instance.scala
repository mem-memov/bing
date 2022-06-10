package memmemov.bing.element

import memmemov.bing.address
import memmemov.bing.block

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  private val level: Int
):
  private lazy val content: block.Instance = new block.Instance
  private lazy val children: Array[Instance] = (new Array[Instance](Instance.size)).map(_ => new Instance(level + 1))
  private lazy val addressSize: Int = scala.math.pow(8, level).toInt

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

object Instance:

  private lazy val size: Int = UByte.MaxValue.toInt + 1