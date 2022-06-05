package memmemov.bing

import scala.scalanative.libc.stdlib.malloc
import scala.scalanative.unsafe.UWord

class Page:

  def line(offset: UWord, size: UWord): Line = Line(offset, size, malloc(size))
