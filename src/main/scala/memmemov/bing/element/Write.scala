package memmemov.bing.element

sealed trait Write
object Written extends Write
object NotWritten extends Write
