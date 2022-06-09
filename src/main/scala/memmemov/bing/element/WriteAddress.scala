package memmemov.bing.element

sealed trait WriteAddress
object WrittenAddress extends WriteAddress
object NotWrittenAddress extends WriteAddress
