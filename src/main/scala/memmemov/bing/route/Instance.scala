package memmemov.bing.route

import memmemov.bing.element
import memmemov.bing.address

import scala.annotation.tailrec
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

class Instance(
  start: element.Instance
):

  def writeAddress(destination: address.Instance, content: address.Instance): List[WriteAddress] =

    @tailrec
    def accumulateResults(destination: address.Instance, content: address.Instance, results: List[WriteAddress]): List[WriteAddress] =
      content.take match
        case address.NotTakenAddressEmpty =>
          results
        case address.Taken(contentPart, shorterContent) =>
          start.write(destination, contentPart) match
            case element.NotWritten =>
              NotWrittenAddress :: results
            case element.Written =>
              accumulateResults(destination.increment, shorterContent, WrittenAddress :: results)

    content.padBig(destination) match
      case address.PaddedBig(paddedContent) =>
        accumulateResults(destination, paddedContent, List.empty[WriteAddress])
      case _ => List.empty[WriteAddress]
    
    

  def readAddress(origin: address.Instance): ReadAddress =

    @tailrec
    def accumulateResults(origin: address.Instance, countDown: Int, results: List[Option[UByte]]): List[Option[UByte]] =
      if countDown == 0 then
        results
      else
        start.read(origin) match
          case element.NotRead =>
            None :: results
          case element.ReadResult(content) =>
            accumulateResults(origin.increment, countDown - 1, Some(content) :: results)

    val trimmedOrigin = origin.trimBig
    val results = accumulateResults(trimmedOrigin, trimmedOrigin.length, List.empty[Option[UByte]])

    if results.contains(None) then
      NotReadAddress
    else
      ReadAddressResult(
        value = new address.Instance(results.map(_.get))
      )