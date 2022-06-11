# bing

This library provides an in-memory storage of addresses.
It enables you to send existing addresses to new places in memory.
You can run through all of them if you wish.
No thread safety is at your disposal.
Start with:

```scala
import memmemov.bing.{Address, Memory}

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

val addresses = new Memory
val firstAddress = memory.start

addresses.append(firstAddress) match
    case memory.Appended(secondAddress) =>
        addresses.append(secondAddress)
        ()
    case _ =>
      ()

memory.foreach { a: Address =>
  a.foreach { b: UByte =>
    // use the byte
    ()
  }
}
```

```bash
sudo apt install clang
sudo apt install libgc-dev # optional

sbt test
```
