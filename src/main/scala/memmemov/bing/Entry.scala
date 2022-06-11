package memmemov.bing

import scala.scalanative.unsigned.UByte

trait Entry:

  def foreach(f: UByte => Unit): Unit