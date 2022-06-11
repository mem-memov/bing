# bing

This library provides an in-memory storage of addresses.
It enables you to send existing addresses to new places in memory.
You can run through all of them if you wish.
No thread safety is at your disposal.
Start with:

```scala
import memmemov.bing

import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

val inventory: bing.Inventory = new Memory
val firstEntry: bing.Entry = memory.start

inventory.append(firstEntry) match
    case inventory.Appended(secondEntry) =>
      inventory.append(secondEntry)
      ()
    case _ =>
      ()

inventory.foreach { entry: bing.Entry =>
  entry.foreach { b: UByte =>
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
