package memmemov.bing.route

import memmemov.bing.element
import memmemov.bing.address

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  start: element.Instance
):

  def writeAddress(where: address.Instance, what: address.Instance): List[WriteAddress] =

    @tailrec
    def accumulateResults(where: address.Instance, what: address.Instance, results: List[WriteAddress]): List[WriteAddress] =
      what.take match
        case address.NotTakenAddressEmpty =>
          results
        case address.Taken(index, shorterAddress) =>
          start.write(where, index) match
            case element.NotWritten =>
              NotWrittenAddress :: results
            case element.Written =>
              accumulateResults(where.increment, shorterAddress, WrittenAddress :: results)

    accumulateResults(where, what, List.empty[WriteAddress])

  def readAddress(where: address.Instance): ReadAddress =

    @tailrec
    def accumulateResults(where: address.Instance, countDown: Int, results: List[Option[UByte]]): List[Option[UByte]] =
      if countDown == 0 then
        results
      else
        start.read(where) match
          case element.NotRead =>
            None :: results
          case element.ReadResult(content) =>
            accumulateResults(where.increment, countDown - 1, Some(content) :: results)

    val trimmedAddress = where.trimBig
    val results = accumulateResults(trimmedAddress, trimmedAddress.length, List.empty[Option[UByte]])

    if results.contains(None) then
      NotReadAddress
    else
      ReadAddressResult(
        value = new address.Instance(results.map(_.get))
      )