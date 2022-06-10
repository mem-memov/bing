package memmemov.bing.route

sealed trait WriteAddress
object WrittenAddress extends WriteAddress
object NotWrittenAddress extends WriteAddress
