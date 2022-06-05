package memmemov.bing

import scala.scalanative.libc.stdlib.malloc
import scala.scalanative.unsafe.UWord

class Page(
          private val lines: List[Line]
          )
