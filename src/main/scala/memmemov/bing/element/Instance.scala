package memmemov.bing.element

import memmemov.bing.address
import memmemov.bing.block

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  private val level: Int
):
  private lazy val content: block.Instance = new block.Instance
  private lazy val children: Array[Instance] = (new Array[Instance](256)).map(_ => new Instance(level + 1))
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

  def writeAddress(where: address.Instance, what: address.Instance): List[WriteAddress] =

    @tailrec
    def accumulateResults(where: address.Instance, what: address.Instance, results: List[WriteAddress]): List[WriteAddress] =
      what.take match
        case address.NotTakenAddressEmpty =>
          results
        case address.Taken(index, shorterAddress) =>
          this.write(where, index) match
            case NotWritten =>
              NotWrittenAddress :: results
            case Written =>
              accumulateResults(where.increment, shorterAddress, WrittenAddress :: results)

    accumulateResults(where, what, List.empty[WriteAddress])

  def readAddress(where: address.Instance): ReadAddress =

    @tailrec
    def accumulateResults(where: address.Instance, countDown: Int, results: List[Option[UByte]]): List[Option[UByte]] =
      if countDown == 0 then
        results
      else
        this.read(where) match
          case NotRead =>
            None :: results
          case ReadResult(content) =>
            accumulateResults(where.increment, countDown - 1, Some(content) :: results)

    val results = accumulateResults(where, addressSize, List.empty[Option[UByte]])

    if results.contains(None) then
      NotReadAddress
    else
      ReadAddressResult(
        value = new address.Instance(results.map(_.get))
      )
