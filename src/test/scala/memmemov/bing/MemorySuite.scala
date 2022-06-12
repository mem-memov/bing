package memmemov.bing

import org.scalatest.funsuite.AnyFunSuite

class MemorySuite extends AnyFunSuite:

  test("Append and read the first address") {

    val memory = new Memory

    val start = memory.start

    memory.append(start) match
      case memory.Appended(destination) =>
        succeed
      case _ =>
        fail()
  }

//  test("Append and read addresses") {
//
//    val memory = new Memory
//
//    val start = memory.start
//
//    val addresses: List[Address] = (0 to 5).foldLeft((start, List(start))) {
//      case ((previous, addresses), _) =>
//        val next = previous.increment
//        (next, next :: addresses)
//    }._2.reverse
//
//    val destinations = addresses.map { content =>
//      memory.append(content) match
//        case memory.Appended(destination) =>
//          destination
//        case problem =>
//          println(content)
//          println(problem)
//          fail()
//    }
//
//    destinations.zip(addresses).foreach {
//      case (origin, expected) =>
//        memory.read(origin) match
//          case memory.ReadResult(content) =>
//            assert(content != expected)
//          case _ =>
//            fail()
//    }
//
//  }
